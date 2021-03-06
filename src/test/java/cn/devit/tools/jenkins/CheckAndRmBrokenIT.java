package cn.devit.tools.jenkins;

import org.junit.Test;

import java.io.File;

import static org.junit.Assert.*;

/**
 * <p>
 *
 * @author lxb
 */
public class CheckAndRmBrokenIT {

    @Test
    public void run(){
        Config config = new Config();
        config.setWorkingDir(new File("jenkins-update-site"));
        new CheckAndRmBroken().run(config);
    }

    @Test
    public void run_target_tmp() throws Exception{

        Config config = new Config();
        config.setWorkingDir(new File("target/temp"));
        new CheckAndRmBroken().run(config);
    }

}