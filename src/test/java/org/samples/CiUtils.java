package org.samples;

import com.spun.util.StringUtils;

import static org.junit.jupiter.api.Assumptions.assumeFalse;

public class CiUtils {
    protected static void skipTestOnCI() {
        assumeFalse(CiUtils.isCI());
    }

    public static boolean isCI() {
        String[] environmentVariablesForCI = {
                // begin-snippet: supported_ci_env_vars
                "CI",
                "CONTINUOUS_INTEGRATION",
                "GITHUB_ACTIONS",
                "GO_SERVER_URL",
                "JENKINS_URL",
                "TEAMCITY_VERSION",
                "TF_BUILD"
                // end-snippet
        };

        for (var variable : environmentVariablesForCI) {
            if (StringUtils.isNonZero(System.getenv(variable))) {
                return true;
            }
        }
        return false;
    }
}
