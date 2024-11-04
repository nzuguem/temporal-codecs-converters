package me.nzuguem.temporal.shared.payloads.codecs;

import io.temporal.api.common.v1.Payload;
import io.temporal.payload.codec.PayloadCodec;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nonnull;
import java.util.List;

public class IdentityCodec implements PayloadCodec {

    private static final Logger LOGGER = LoggerFactory.getLogger(IdentityCodec.class);

    @Nonnull
    @Override
    public List<Payload> encode(@Nonnull List<Payload> payloads) {
        LOGGER.atInfo()
                .log("Encoding...");
        return payloads;
    }

    @Nonnull
    @Override
    public List<Payload> decode(@Nonnull List<Payload> payloads) {
        LOGGER.atInfo()
                .log("Decoding...");
        return payloads;
    }
}
