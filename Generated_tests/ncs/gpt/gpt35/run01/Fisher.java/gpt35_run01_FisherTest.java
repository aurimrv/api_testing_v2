
    @Test
    public void testFisherMethod() {
        double result = org.restncs.imp.Fisher.exe(4, 5, 0.6);
        assertEquals(0.3204541099824456, result, 0.0001);
    }

    @Test
    public void testFisherMethodBoundaryCase1() {
        double result = org.restncs.imp.Fisher.exe(1, 1, 1.0); // Testing boundary case a=1, b=1
        assertEquals(0.5000000000254615, result, 0.0001);
    }

    @Test
    public void testFisherMethodBoundaryCase2() {
        double result = org.restncs.imp.Fisher.exe(1, 2, 0.5); // Testing boundary case a=1, b=2
        assertEquals(0.4472135954999579, result, 0.0001);
    }

    @Test
    public void testFisherMethodBoundaryCase3() {
        double result = org.restncs.imp.Fisher.exe(2, 1, 2.0); // Testing boundary case a=2, b=1
        assertEquals(0.5527864045000421, result, 0.0001);
    }

    @Test
    public void testFisherMethodBoundaryCase4() {
        double result = org.restncs.imp.Fisher.exe(3, 3, 1.5); // Testing boundary case a=3, b=3
        assertEquals(0.6264699609795708, result, 0.0001);
    }

    @Test
    public void testFisherMethodExceptionCase1() {
        double result = org.restncs.imp.Fisher.exe(0, 0, 0.0); // Testing exception case with division by zero
        assertEquals(Double.NaN, result, 0.0001);
    }

    @Test
    public void testFisherMethodExceptionCase2() {
        double result = org.restncs.imp.Fisher.exe(6, 4, 1.2); // Testing exception case with unexpected input
        assertEquals(0.5503175760099959, result, 0.0001);
    }
