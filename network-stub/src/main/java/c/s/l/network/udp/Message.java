package c.s.l.network.udp;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Message implements Serializable {

    private static final long serialVersionUID = -5193133261732526643L;
    private byte cmd;
    private int userId;
    private String msg;
    private String data;
}
