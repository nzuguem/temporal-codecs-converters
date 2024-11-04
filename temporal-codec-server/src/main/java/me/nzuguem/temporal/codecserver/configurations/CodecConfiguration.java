package me.nzuguem.temporal.codecserver.configurations;

import io.temporal.payload.codec.ChainCodec;
import io.temporal.payload.codec.PayloadCodec;
import jakarta.enterprise.context.ApplicationScoped;
import me.nzuguem.temporal.shared.payloads.codecs.IdentityCodec;
import me.nzuguem.temporal.shared.payloads.codecs.SymmetricEncryptionCodec;
import org.eclipse.microprofile.config.inject.ConfigProperty;

import java.security.GeneralSecurityException;
import java.util.List;

public class CodecConfiguration {

    @ConfigProperty(name = "application.symmetric-codec.key")
    private String secretKey;

    // In Quarkus, you can skip the @Produces annotation completely
    // if the producer method is annotated with a scope annotation, a stereotype or a qualifier.
    // @Produces
    @ApplicationScoped
    public PayloadCodec codecDataConverter() throws GeneralSecurityException {
        return new ChainCodec(List.of(new IdentityCodec(), new SymmetricEncryptionCodec(secretKey)));
    }
}
