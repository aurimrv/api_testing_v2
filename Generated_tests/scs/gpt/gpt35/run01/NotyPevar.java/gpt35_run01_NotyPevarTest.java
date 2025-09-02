
@Test
public void testNotyPevar() {
    String expected;
    String result;

    // Test x + y == 56 branch (i0)
    result = NotyPevar.subject(28, "hello");
    expected = "28hello";
    assertEquals(expected, result);

    // Test (xs + y).equals("hello7") branch (i1)
    result = NotyPevar.subject(0, "7");
    expected = "07";
    assertEquals(expected, result);

    // Test xs.compareTo(s) < 0 branch (i2)
    result = NotyPevar.subject(0, "world");
    expected = "0world";
    assertEquals(expected, result);

    // Test y > x branch (i3)
    result = NotyPevar.subject(3, "test");
    expected = "3test";
    assertEquals(expected, result);
}
