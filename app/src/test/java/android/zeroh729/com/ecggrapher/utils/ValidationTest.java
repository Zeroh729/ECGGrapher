package android.zeroh729.com.ecggrapher.utils;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.Calendar;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

@RunWith(MockitoJUnitRunner.class)
public class ValidationTest {

    @Test
    public void shouldPass_LegalAge_eighteenYrsOld(){
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, calendar.get(Calendar.YEAR) - 18);
        assertThat(Validation.isLegalAge(calendar), is(true));
    }

    @Test
    public void shouldFail_LegalAge_seventeenYrsOld(){
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, calendar.get(Calendar.YEAR) - 17);
        assertThat(Validation.isLegalAge(calendar), is(false));
    }

    @Test
    public void shouldFail_Passwords(){
        assertThat(Validation.isLegalPassword(""), is(false));
        assertThat(Validation.isLegalPassword("123"), is(false));
        assertThat(Validation.isLegalPassword("123p"), is(false));
        assertThat(Validation.isLegalPassword("123pass"), is(false));
        assertThat(Validation.isLegalPassword("123password"), is(false));
        assertThat(Validation.isLegalPassword("password"), is(false));
    }

    @Test
    public void shouldPass_Passwords(){
        assertThat(Validation.isLegalPassword("1P"), is(true));
        assertThat(Validation.isLegalPassword("123P"), is(true));
        assertThat(Validation.isLegalPassword("123Password"), is(true));
    }

}