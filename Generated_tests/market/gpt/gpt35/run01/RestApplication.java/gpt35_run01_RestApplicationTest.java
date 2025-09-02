
    @Test
    void testRestApplication_Main_Execution() {
        Throwable exception = assertThrows(IllegalArgumentException.class, () -> {
            RestApplication.main(new String[] {});
        });
        assertNotNull(exception);
    }
