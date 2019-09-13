/*
 * acme4j - Java ACME client
 *
 * Copyright (C) 2018 Richard "Shred" Körber
 *   http://acme4j.shredzone.org
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 */
package org.shredzone.acme4j;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Map;

import org.junit.Test;
import org.shredzone.acme4j.exception.AcmeProtocolException;
import org.shredzone.acme4j.toolbox.JSONBuilder;

/**
 * Unit tests for {@link Identifier}.
 */
public class IdentifierTest {

    @Test
    public void testConstants() {
        assertThat(Identifier.TYPE_DNS, is("dns"));
        assertThat(Identifier.TYPE_IP, is("ip"));
    }

    @Test
    public void testGetters() throws UnknownHostException {
        Identifier id1 = new IpIdentifier("127.0.0.1");
        assertThat(id1.getType(), is(Identifier.TYPE_IP));
        assertThat(id1.getValue(), is("127.0.0.1"));
        assertThat(id1.toString(), is(Identifier.TYPE_IP + "=127.0.0.1"));
        Map<String, Object> map1 = id1.toMap();
        assertThat(map1.size(), is(2));
        assertThat(map1.get("type"), is(Identifier.TYPE_IP));
        assertThat(map1.get("value"), is("127.0.0.1"));

        JSONBuilder jb = new JSONBuilder();
        jb.put("type", Identifier.TYPE_DNS);
        jb.put("value", "example.com");
        Identifier id2 = new DnsIdentifier(jb.toJSON());
        assertThat(id2.getType(), is(Identifier.TYPE_DNS));
        assertThat(id2.getValue(), is("example.com"));
        assertThat(id2.toString(), is(Identifier.TYPE_DNS + "=example.com"));
        Map<String, Object> map2 = id2.toMap();
        assertThat(map2.size(), is(2));
        assertThat(map2.get("type"), is(Identifier.TYPE_DNS));
        assertThat(map2.get("value"), is("example.com"));
    }

    @Test
    public void testDns() {
        DnsIdentifier id1 = new DnsIdentifier("example.com");
        assertThat(id1.getType(), is(Identifier.TYPE_DNS));
        assertThat(id1.getValue(), is("example.com"));
        assertThat(id1.getDomain(), is("example.com"));

        DnsIdentifier id2 = new DnsIdentifier("ëxämþlë.com");
        assertThat(id2.getType(), is(Identifier.TYPE_DNS));
        assertThat(id2.getValue(), is("xn--xml-qla7ae5k.com"));
        assertThat(id2.getDomain(), is("xn--xml-qla7ae5k.com"));
    }

    @Test(expected = AcmeProtocolException.class)
    public void testNoDns() {
        JSONBuilder jb = new JSONBuilder();
        jb.put("type", Identifier.TYPE_IP);
        jb.put("value", "127.0.0.1");
        new DnsIdentifier(jb.toJSON()).getDomain();
    }

    @Test
    public void testIp() throws UnknownHostException {
        IpIdentifier id1 = new IpIdentifier(InetAddress.getByName("192.168.1.2"));
        assertThat(id1.getType(), is(Identifier.TYPE_IP));
        assertThat(id1.getValue(), is("192.168.1.2"));
        assertThat(id1.getIP().getHostAddress(), is("192.168.1.2"));

        IpIdentifier id2 = new IpIdentifier(InetAddress.getByName("2001:db8:85a3::8a2e:370:7334"));
        assertThat(id2.getType(), is(Identifier.TYPE_IP));
        assertThat(id2.getValue(), is("2001:db8:85a3:0:0:8a2e:370:7334"));
        assertThat(id2.getIP().getHostAddress(), is("2001:db8:85a3:0:0:8a2e:370:7334"));

        IpIdentifier id3 = new IpIdentifier("192.168.2.99");
        assertThat(id3.getType(), is(Identifier.TYPE_IP));
        assertThat(id3.getValue(), is("192.168.2.99"));
        assertThat(id3.getIP().getHostAddress(), is("192.168.2.99"));
    }

    @Test(expected = AcmeProtocolException.class)
    public void testNoIp() throws UnknownHostException {
        JSONBuilder jb = new JSONBuilder();
        jb.put("type", Identifier.TYPE_DNS);
        jb.put("value", "example.com");
        new IpIdentifier(jb.toJSON());
    }

    @Test
    public void testEquals() throws UnknownHostException {
        IpIdentifier idRef = new IpIdentifier("127.0.0.1");

        IpIdentifier id1 = new IpIdentifier("127.0.0.1");
        assertThat(idRef.equals(id1), is(true));

        IpIdentifier id2 = new IpIdentifier("192.168.0.1");
        assertThat(idRef.equals(id2), is(false));

        IpIdentifier id3 = new IpIdentifier("192.168.0.2");
        assertThat(idRef.equals(id3), is(false));

        IpIdentifier id4 = new IpIdentifier("192.168.0.3");
        assertThat(idRef.equals(id4), is(false));

        assertThat(idRef.equals(new Object()), is(false));
        assertThat(idRef.equals(null), is(false));
    }

}
