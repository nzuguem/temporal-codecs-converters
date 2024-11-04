package me.nzuguem.temporal.codecserver.services;

import io.temporal.api.common.v1.Payload;
import io.temporal.api.common.v1.Payloads;
import io.temporal.payload.codec.PayloadCodec;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.List;
import java.util.function.Function;
import java.util.function.UnaryOperator;

@ApplicationScoped
public class RDEService {

    private final PayloadCodec codec;

    public RDEService(PayloadCodec codec) {
        this.codec = codec;
    }

    public List<Payload> encode(List<Payload> payloads) {
        return this.codec.encode(payloads);
    }

    public List<Payload> decode(List<Payload> payloads) {
        return this.codec.decode(payloads);
    }
}
