import org.apache.logging.log4j.core.config.yaml.YamlConfigurationFactory;

/**
 * cmds
 * Created by yeti on 16/2/29.
 */
public class Launcher {

	static {
		System.setProperty(YamlConfigurationFactory.CONFIGURATION_FILE_PROPERTY, "logger.yaml");
	}

	public static void main(String... args) {
	}
}
