package com.mycompany.myapp.domain;

import static org.assertj.core.api.Assertions.assertThat;

public class PartnerAsserts {

    /**
     * Asserts that the entity has all properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertPartnerAllPropertiesEquals(Partner expected, Partner actual) {
        assertPartnerAutoGeneratedPropertiesEquals(expected, actual);
        assertPartnerAllUpdatablePropertiesEquals(expected, actual);
    }

    /**
     * Asserts that the entity has all updatable properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertPartnerAllUpdatablePropertiesEquals(Partner expected, Partner actual) {
        assertPartnerUpdatableFieldsEquals(expected, actual);
        assertPartnerUpdatableRelationshipsEquals(expected, actual);
    }

    /**
     * Asserts that the entity has all the auto generated properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertPartnerAutoGeneratedPropertiesEquals(Partner expected, Partner actual) {
        assertThat(expected)
            .as("Verify Partner auto generated properties")
            .satisfies(e -> assertThat(e.getId()).as("check id").isEqualTo(actual.getId()));
    }

    /**
     * Asserts that the entity has all the updatable fields set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertPartnerUpdatableFieldsEquals(Partner expected, Partner actual) {
        assertThat(expected)
            .as("Verify Partner relevant properties")
            .satisfies(e -> assertThat(e.getCodep()).as("check codep").isEqualTo(actual.getCodep()))
            .satisfies(e -> assertThat(e.getType()).as("check type").isEqualTo(actual.getType()))
            .satisfies(e -> assertThat(e.getName()).as("check name").isEqualTo(actual.getName()))
            .satisfies(e -> assertThat(e.getContact()).as("check contact").isEqualTo(actual.getContact()))
            .satisfies(e -> assertThat(e.getLogo()).as("check logo").isEqualTo(actual.getLogo()))
            .satisfies(e -> assertThat(e.getLogoContentType()).as("check logo contenty type").isEqualTo(actual.getLogoContentType()))
            .satisfies(e -> assertThat(e.getIcon()).as("check icon").isEqualTo(actual.getIcon()))
            .satisfies(e -> assertThat(e.getIconContentType()).as("check icon contenty type").isEqualTo(actual.getIconContentType()));
    }

    /**
     * Asserts that the entity has all the updatable relationships set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertPartnerUpdatableRelationshipsEquals(Partner expected, Partner actual) {}
}
