package com.mebank;

import org.junit.Test;

import java.io.FileNotFoundException;

public class MEBankApplicationTest {

    @Test(expected = FileNotFoundException.class)
    public void testApplication_FileNotFound() throws Exception {

        MEBankApplication.main(new String[]{"/path/not/found"});
    }

}
