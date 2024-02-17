package org.springframework.samples.petclinic.sfg;


import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes = {BaseConfig.class, YannyConfig.class})
public class HearingInterpreterTestForYannyJunit4 {



    @Autowired
    HearingInterpreter hearingInterpreter;

//    @Before
//    public void setUp() throws Exception {
//        hearingInterpreter = new HearingInterpreter(new LaurelWordProducer());
//    }

    @Test
    public void whatIHeard() {
        String word = hearingInterpreter.whatIHeard();;

        assertThat(word).isEqualTo("Yanny");
    }
}
