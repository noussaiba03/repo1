package com.mycompany.myapp.web.rest;

import static com.mycompany.myapp.domain.PartnerAsserts.*;
import static com.mycompany.myapp.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mycompany.myapp.IntegrationTest;
import com.mycompany.myapp.domain.Partner;
import com.mycompany.myapp.repository.EntityManager;
import com.mycompany.myapp.repository.PartnerRepository;
import java.util.Base64;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.reactive.server.WebTestClient;

/**
 * Integration tests for the {@link PartnerResource} REST controller.
 */
@IntegrationTest
@AutoConfigureWebTestClient(timeout = IntegrationTest.DEFAULT_ENTITY_TIMEOUT)
@WithMockUser
class PartnerResourceIT {

    private static final String DEFAULT_CODEP = "AAAAAAAAAA";
    private static final String UPDATED_CODEP = "BBBBBBBBBB";

    private static final String DEFAULT_TYPE = "AAAAAAAAAA";
    private static final String UPDATED_TYPE = "BBBBBBBBBB";

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_CONTACT = "AAAAAAAAAA";
    private static final String UPDATED_CONTACT = "BBBBBBBBBB";

    private static final byte[] DEFAULT_LOGO = TestUtil.createByteArray(1, "0");
    private static final byte[] UPDATED_LOGO = TestUtil.createByteArray(1, "1");
    private static final String DEFAULT_LOGO_CONTENT_TYPE = "image/jpg";
    private static final String UPDATED_LOGO_CONTENT_TYPE = "image/png";

    private static final byte[] DEFAULT_ICON = TestUtil.createByteArray(1, "0");
    private static final byte[] UPDATED_ICON = TestUtil.createByteArray(1, "1");
    private static final String DEFAULT_ICON_CONTENT_TYPE = "image/jpg";
    private static final String UPDATED_ICON_CONTENT_TYPE = "image/png";

    private static final String ENTITY_API_URL = "/api/partners";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private PartnerRepository partnerRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private WebTestClient webTestClient;

    private Partner partner;

    private Partner insertedPartner;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Partner createEntity(EntityManager em) {
        Partner partner = new Partner()
            .codep(DEFAULT_CODEP)
            .type(DEFAULT_TYPE)
            .name(DEFAULT_NAME)
            .contact(DEFAULT_CONTACT)
            .logo(DEFAULT_LOGO)
            .logoContentType(DEFAULT_LOGO_CONTENT_TYPE)
            .icon(DEFAULT_ICON)
            .iconContentType(DEFAULT_ICON_CONTENT_TYPE);
        return partner;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Partner createUpdatedEntity(EntityManager em) {
        Partner partner = new Partner()
            .codep(UPDATED_CODEP)
            .type(UPDATED_TYPE)
            .name(UPDATED_NAME)
            .contact(UPDATED_CONTACT)
            .logo(UPDATED_LOGO)
            .logoContentType(UPDATED_LOGO_CONTENT_TYPE)
            .icon(UPDATED_ICON)
            .iconContentType(UPDATED_ICON_CONTENT_TYPE);
        return partner;
    }

    public static void deleteEntities(EntityManager em) {
        try {
            em.deleteAll(Partner.class).block();
        } catch (Exception e) {
            // It can fail, if other entities are still referring this - it will be removed later.
        }
    }

    @BeforeEach
    public void initTest() {
        partner = createEntity(em);
    }

    @AfterEach
    public void cleanup() {
        if (insertedPartner != null) {
            partnerRepository.delete(insertedPartner).block();
            insertedPartner = null;
        }
        deleteEntities(em);
    }

    @Test
    void createPartner() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Partner
        var returnedPartner = webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(partner))
            .exchange()
            .expectStatus()
            .isCreated()
            .expectBody(Partner.class)
            .returnResult()
            .getResponseBody();

        // Validate the Partner in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertPartnerUpdatableFieldsEquals(returnedPartner, getPersistedPartner(returnedPartner));

        insertedPartner = returnedPartner;
    }

    @Test
    void createPartnerWithExistingId() throws Exception {
        // Create the Partner with an existing ID
        partner.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(partner))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Partner in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    void checkCodepIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        partner.setCodep(null);

        // Create the Partner, which fails.

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(partner))
            .exchange()
            .expectStatus()
            .isBadRequest();

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    void checkTypeIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        partner.setType(null);

        // Create the Partner, which fails.

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(partner))
            .exchange()
            .expectStatus()
            .isBadRequest();

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    void checkNameIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        partner.setName(null);

        // Create the Partner, which fails.

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(partner))
            .exchange()
            .expectStatus()
            .isBadRequest();

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    void checkContactIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        partner.setContact(null);

        // Create the Partner, which fails.

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(partner))
            .exchange()
            .expectStatus()
            .isBadRequest();

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    void getAllPartners() {
        // Initialize the database
        insertedPartner = partnerRepository.save(partner).block();

        // Get all the partnerList
        webTestClient
            .get()
            .uri(ENTITY_API_URL + "?sort=id,desc")
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.[*].id")
            .value(hasItem(partner.getId().intValue()))
            .jsonPath("$.[*].codep")
            .value(hasItem(DEFAULT_CODEP))
            .jsonPath("$.[*].type")
            .value(hasItem(DEFAULT_TYPE))
            .jsonPath("$.[*].name")
            .value(hasItem(DEFAULT_NAME))
            .jsonPath("$.[*].contact")
            .value(hasItem(DEFAULT_CONTACT))
            .jsonPath("$.[*].logoContentType")
            .value(hasItem(DEFAULT_LOGO_CONTENT_TYPE))
            .jsonPath("$.[*].logo")
            .value(hasItem(Base64.getEncoder().encodeToString(DEFAULT_LOGO)))
            .jsonPath("$.[*].iconContentType")
            .value(hasItem(DEFAULT_ICON_CONTENT_TYPE))
            .jsonPath("$.[*].icon")
            .value(hasItem(Base64.getEncoder().encodeToString(DEFAULT_ICON)));
    }

    @Test
    void getPartner() {
        // Initialize the database
        insertedPartner = partnerRepository.save(partner).block();

        // Get the partner
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, partner.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.id")
            .value(is(partner.getId().intValue()))
            .jsonPath("$.codep")
            .value(is(DEFAULT_CODEP))
            .jsonPath("$.type")
            .value(is(DEFAULT_TYPE))
            .jsonPath("$.name")
            .value(is(DEFAULT_NAME))
            .jsonPath("$.contact")
            .value(is(DEFAULT_CONTACT))
            .jsonPath("$.logoContentType")
            .value(is(DEFAULT_LOGO_CONTENT_TYPE))
            .jsonPath("$.logo")
            .value(is(Base64.getEncoder().encodeToString(DEFAULT_LOGO)))
            .jsonPath("$.iconContentType")
            .value(is(DEFAULT_ICON_CONTENT_TYPE))
            .jsonPath("$.icon")
            .value(is(Base64.getEncoder().encodeToString(DEFAULT_ICON)));
    }

    @Test
    void getNonExistingPartner() {
        // Get the partner
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, Long.MAX_VALUE)
            .accept(MediaType.APPLICATION_PROBLEM_JSON)
            .exchange()
            .expectStatus()
            .isNotFound();
    }

    @Test
    void putExistingPartner() throws Exception {
        // Initialize the database
        insertedPartner = partnerRepository.save(partner).block();

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the partner
        Partner updatedPartner = partnerRepository.findById(partner.getId()).block();
        updatedPartner
            .codep(UPDATED_CODEP)
            .type(UPDATED_TYPE)
            .name(UPDATED_NAME)
            .contact(UPDATED_CONTACT)
            .logo(UPDATED_LOGO)
            .logoContentType(UPDATED_LOGO_CONTENT_TYPE)
            .icon(UPDATED_ICON)
            .iconContentType(UPDATED_ICON_CONTENT_TYPE);

        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, updatedPartner.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(updatedPartner))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Partner in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedPartnerToMatchAllProperties(updatedPartner);
    }

    @Test
    void putNonExistingPartner() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        partner.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, partner.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(partner))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Partner in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithIdMismatchPartner() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        partner.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, longCount.incrementAndGet())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(partner))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Partner in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithMissingIdPathParamPartner() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        partner.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(partner))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the Partner in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void partialUpdatePartnerWithPatch() throws Exception {
        // Initialize the database
        insertedPartner = partnerRepository.save(partner).block();

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the partner using partial update
        Partner partialUpdatedPartner = new Partner();
        partialUpdatedPartner.setId(partner.getId());

        partialUpdatedPartner
            .codep(UPDATED_CODEP)
            .name(UPDATED_NAME)
            .contact(UPDATED_CONTACT)
            .logo(UPDATED_LOGO)
            .logoContentType(UPDATED_LOGO_CONTENT_TYPE)
            .icon(UPDATED_ICON)
            .iconContentType(UPDATED_ICON_CONTENT_TYPE);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedPartner.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(om.writeValueAsBytes(partialUpdatedPartner))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Partner in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPartnerUpdatableFieldsEquals(createUpdateProxyForBean(partialUpdatedPartner, partner), getPersistedPartner(partner));
    }

    @Test
    void fullUpdatePartnerWithPatch() throws Exception {
        // Initialize the database
        insertedPartner = partnerRepository.save(partner).block();

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the partner using partial update
        Partner partialUpdatedPartner = new Partner();
        partialUpdatedPartner.setId(partner.getId());

        partialUpdatedPartner
            .codep(UPDATED_CODEP)
            .type(UPDATED_TYPE)
            .name(UPDATED_NAME)
            .contact(UPDATED_CONTACT)
            .logo(UPDATED_LOGO)
            .logoContentType(UPDATED_LOGO_CONTENT_TYPE)
            .icon(UPDATED_ICON)
            .iconContentType(UPDATED_ICON_CONTENT_TYPE);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedPartner.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(om.writeValueAsBytes(partialUpdatedPartner))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Partner in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPartnerUpdatableFieldsEquals(partialUpdatedPartner, getPersistedPartner(partialUpdatedPartner));
    }

    @Test
    void patchNonExistingPartner() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        partner.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partner.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(om.writeValueAsBytes(partner))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Partner in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithIdMismatchPartner() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        partner.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, longCount.incrementAndGet())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(om.writeValueAsBytes(partner))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Partner in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithMissingIdPathParamPartner() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        partner.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(om.writeValueAsBytes(partner))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the Partner in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void deletePartner() {
        // Initialize the database
        insertedPartner = partnerRepository.save(partner).block();

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the partner
        webTestClient
            .delete()
            .uri(ENTITY_API_URL_ID, partner.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNoContent();

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return partnerRepository.count().block();
    }

    protected void assertIncrementedRepositoryCount(long countBefore) {
        assertThat(countBefore + 1).isEqualTo(getRepositoryCount());
    }

    protected void assertDecrementedRepositoryCount(long countBefore) {
        assertThat(countBefore - 1).isEqualTo(getRepositoryCount());
    }

    protected void assertSameRepositoryCount(long countBefore) {
        assertThat(countBefore).isEqualTo(getRepositoryCount());
    }

    protected Partner getPersistedPartner(Partner partner) {
        return partnerRepository.findById(partner.getId()).block();
    }

    protected void assertPersistedPartnerToMatchAllProperties(Partner expectedPartner) {
        // Test fails because reactive api returns an empty object instead of null
        // assertPartnerAllPropertiesEquals(expectedPartner, getPersistedPartner(expectedPartner));
        assertPartnerUpdatableFieldsEquals(expectedPartner, getPersistedPartner(expectedPartner));
    }

    protected void assertPersistedPartnerToMatchUpdatableProperties(Partner expectedPartner) {
        // Test fails because reactive api returns an empty object instead of null
        // assertPartnerAllUpdatablePropertiesEquals(expectedPartner, getPersistedPartner(expectedPartner));
        assertPartnerUpdatableFieldsEquals(expectedPartner, getPersistedPartner(expectedPartner));
    }
}
