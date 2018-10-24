package c.s.l.network.paket;

import c.s.l.network.proto.MessageStub;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.net.SocketAddress;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MessagePacket implements Serializable {
    private static final long serialVersionUID = 5578925844693290234L;

    private SocketAddress sender;
    private MessageStub.Message message;

}
