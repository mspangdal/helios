/**
 * Copyright (C) 2013 Spotify AB
 */

package com.spotify.helios.common;

import com.spotify.helios.TestBase;

import org.junit.Before;
import org.junit.Test;

import java.security.MessageDigest;
import java.util.LinkedHashMap;
import java.util.Map;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

public class JsonTest extends TestBase {

  static final String EXPECTED_JSON =
      "{\"c\":\"bar\",\"foo\":{\"a\":\"hello\",\"b\":17,\"c\":{\"1\":1,\"2\":\"two\"}}}";

  MessageDigest sha1;
  byte[] expectedDigest;

  @Before
  public void setup() throws Exception {
    sha1 = MessageDigest.getInstance("SHA-1");
    expectedDigest = sha1.digest(EXPECTED_JSON.getBytes());
  }

  public static class Foo {

    public int b;
    public String a;
    public Map<String, Object> c;
  }

  public static class Bar {

    public String c;
    public Foo foo;
  }

  @Test
  public void testObjectSha1() throws Exception {
    final Bar bar = new Bar() {{
      c = "bar";
      foo = new Foo() {{
        b = 17;
        a = "hello";
        c = new LinkedHashMap<String, Object>() {{
          put("2", "two");
          put("1", 1);
        }};
      }};
    }};
    final String barJson = Json.asString(bar);
    assertEquals(EXPECTED_JSON, barJson);
    final byte[] digest = Json.sha1digest(bar);
    assertArrayEquals(expectedDigest, digest);
  }
}