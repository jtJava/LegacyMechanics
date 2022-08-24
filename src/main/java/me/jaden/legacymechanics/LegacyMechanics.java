package me.jaden.legacymechanics;

import com.viaversion.fabric.common.handler.CommonTransformer;
import com.viaversion.fabric.common.handler.FabricDecodeHandler;
import com.viaversion.viaversion.api.connection.ProtocolInfo;
import com.viaversion.viaversion.api.protocol.version.ProtocolVersion;
import io.netty.channel.ChannelHandler;
import me.jaden.legacymechanics.mixin.accessors.ClientConnectionAccessor;
import net.fabricmc.api.ModInitializer;
import net.minecraft.client.MinecraftClient;

public class LegacyMechanics implements ModInitializer {
    private static LegacyMechanics INSTANCE;

    @Override
    public void onInitialize() {
        INSTANCE = this;
    }

    public boolean isLegacyActive() {
        try {
            Class.forName("com.viaversion.viaversion.api.Via");
        } catch (ClassNotFoundException exception) {
            return false;
        }

        @SuppressWarnings("ConstantConditions")
        ChannelHandler viaDecoder = ((ClientConnectionAccessor) MinecraftClient.getInstance().getNetworkHandler()
                .getConnection()).getChannel().pipeline().get(CommonTransformer.HANDLER_DECODER_NAME);

        if (!(viaDecoder instanceof FabricDecodeHandler)) {
            return false;
        }

        ProtocolInfo protocol = ((FabricDecodeHandler) viaDecoder).getInfo().getProtocolInfo();
        if (protocol != null) {
            return ProtocolVersion.getProtocol(protocol.getServerProtocolVersion()).getVersion() == 47;
        } else {
            return false;
        }
    }

    public static LegacyMechanics getInstance() {
        return INSTANCE;
    }
}
