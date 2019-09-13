package org.shredzone.acme4j;

import static org.shredzone.acme4j.toolbox.AcmeUtils.toAce;

import org.shredzone.acme4j.exception.AcmeProtocolException;
import org.shredzone.acme4j.toolbox.JSON;

public class DnsIdentifier extends Identifier {
    private static final long serialVersionUID = -5772607280321122023L;

    /**
     * Creates a new {@link DnsIdentifier} for the given domain name.
     *
     * @param domain
     *        Domain name. Unicode domains are automatically ASCII encoded.
     * @return New {@link Identifier}
     */
    public DnsIdentifier(String value) {
        super(TYPE_DNS, toAce(value));
    }

    /**
     * Creates a new {@link DnsIdentifier} from the given {@link JSON} structure.
     *
     * @param json
     *        {@link JSON} containing the identifier data
     */
    public DnsIdentifier(JSON json) {
        this(json.get(KEY_VALUE).asString());

        String _type = json.get(KEY_TYPE).asString();
        if (!TYPE_DNS.equals(_type)) {
            throw new AcmeProtocolException("expected 'dns' identifier, but found '" + _type + "'");
        }
    }

    /**
     * Returns the domain name of this DNS identifier.
     *
     * @return Domain name. Unicode domains are ASCII encoded.
     */
    public String getDomain() {
        return value;
    }

}
