package me.nzuguem.temporal.codecserver.handler;

import com.google.protobuf.InvalidProtocolBufferException;
import com.google.protobuf.util.JsonFormat;
import io.quarkus.logging.Log;
import io.temporal.api.common.v1.Payload;
import io.temporal.api.common.v1.Payloads;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.ws.rs.HeaderParam;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.HttpHeaders;
import jakarta.ws.rs.core.MediaType;
import me.nzuguem.temporal.codecserver.services.RDEService;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.List;
import java.util.Objects;
import java.util.function.UnaryOperator;

@Path("/")
public class RDEHandler {

    private final RDEService rdeService;

    @Context
    HttpServletRequest httpServletRequest;

    @Context
    HttpServletResponse httpServletResponse;

    public RDEHandler(RDEService rdeService) {
        this.rdeService = rdeService;
    }

    @POST
    @Path("/encode")
    public void encode(@HeaderParam("X-Namespace") String namespace) throws IOException {
        Log.infof("Request for encoding - Namespace: %s", namespace);
        this.process(this.rdeService::encode);
    }

    @POST
    @Path("/decode")
    public void decode(@HeaderParam("X-Namespace") String namespace) throws IOException {
        Log.infof("Request for decoding - Namespace: %s", namespace);
        this.process(this.rdeService::decode);
    }

    private void process(UnaryOperator<List<Payload>> payloadOperation) throws IOException {

        var contentType = httpServletRequest.getContentType();
        if (Objects.isNull(contentType) || !Objects.equals(contentType, MediaType.APPLICATION_JSON)) {
            httpServletResponse.sendError(
                    HttpServletResponse.SC_UNSUPPORTED_MEDIA_TYPE,
                    "Unsupported content type, application/json is expected");
            return;
        }

        var incomingPayloads = Payloads.newBuilder();
        var inputStream = httpServletRequest.getInputStream();
        try (var ioReader = new InputStreamReader(inputStream)) {
            JsonFormat.parser()
                    .merge(ioReader, incomingPayloads);
        } catch (InvalidProtocolBufferException e) {
            httpServletResponse.sendError(HttpServletResponse.SC_BAD_REQUEST, e.getMessage());
            return;
        }

        var incomingPayloadsList = incomingPayloads.build().getPayloadsList();
        var outgoingPayloadsList = payloadOperation.apply(incomingPayloadsList);

        httpServletResponse.addHeader(
                HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON);
        httpServletResponse.setStatus(HttpServletResponse.SC_OK);

        var outputStream = httpServletResponse.getOutputStream();
        try (var out = new OutputStreamWriter(outputStream)) {
            JsonFormat.printer()
                    .appendTo(
                    Payloads.newBuilder().addAllPayloads(outgoingPayloadsList).build(), out);
        }

    }
}
