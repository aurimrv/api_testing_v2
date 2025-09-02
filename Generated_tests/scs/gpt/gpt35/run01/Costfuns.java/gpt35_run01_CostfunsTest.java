
@Test
public void testSubject() {
    // Test all branches in the subject method
    assertEquals("1", Costfuns.subject(5, "baab"));
    assertEquals("2", Costfuns.subject(-445, "babbaba"));
    assertEquals("3", Costfuns.subject(-333, "babbabab"));
    assertEquals("4", Costfuns.subject(667, "babbab"));
    assertEquals("5", Costfuns.subject(555, ""));
    assertEquals("6", Costfuns.subject(-4, ""));
    assertEquals("7", Costfuns.subject(0, ""));
    assertEquals("8", Costfuns.subject(0, ""));
    assertEquals("9", Costfuns.subject(0, ""));
    assertEquals("10", Costfuns.subject(0, ""));
}
