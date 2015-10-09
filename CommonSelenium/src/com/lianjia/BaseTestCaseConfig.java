
package com.lianjia;

public interface BaseTestCaseConfig extends HasProperties {

    /**
     * Gets the a test environment property by name, as opposed to test property.
     * @param name The property name.
     * @return The value for the specified property.
     */
    public String getEnvProperty(String name);
}
