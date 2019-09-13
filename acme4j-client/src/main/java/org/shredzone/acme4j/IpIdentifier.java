package org.shredzone.acme4j;

import java.net.InetAddress;
import java.net.UnknownHostException;

import org.shredzone.acme4j.exception.AcmeProtocolException;
import org.shredzone.acme4j.toolbox.JSON;

public class IpIdentifier extends Identifier {
    private static final long serialVersionUID = -4994894560747698158L;

    /**
     * Creates a new IP identifier for the given {@link InetAddress}.
     *
     * @param ip
     *        {@link InetAddress}
     * @return New {@link Identifier}
     */
    public IpIdentifier(InetAddress ip) {
        super(TYPE_IP, ip.getHostAddress());
    }

    /**
     * Creates a new IP identifier for the given {@link InetAddress}.
     *
     * @param ip
     *        IP address as {@link String}
     * @return New {@link Identifier}
     * @throws UnknownHostException
     * @since 2.7
     */
    public IpIdentifier(String ip) throws UnknownHostException {
        this(InetAddress.getByName(ip));
    }

    /**
     * Creates a new {@link IpIdentifier} from the given {@link JSON} structure.
     *
     * @param json
     *        {@link JSON} containing the identifier data
     * @throws UnknownHostException
     */
    public IpIdentifier(JSON json) throws UnknownHostException {
        this(json.get(KEY_VALUE).asString());

        String _type = json.get(KEY_TYPE).asString();
        if (!TYPE_IP.equals(_type)) {
            throw new AcmeProtocolException("expected 'ip' identifier, but found '" + _type + "'");
        }
    }

    /**
     * Returns the IP address of this IP identifier.
     *
     * @return {@link InetAddress}
     * @throws AcmeProtocolException
     *         if this identifier has a bad ip as its value
     */
    public InetAddress getIP() {
        try {
            return InetAddress.getByName(value);
        } catch (UnknownHostException ex) {
            throw new AcmeProtocolException("bad ip identifier value", ex);
        }
    }
}
