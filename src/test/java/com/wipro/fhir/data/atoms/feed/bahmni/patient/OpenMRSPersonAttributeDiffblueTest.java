package com.wipro.fhir.data.atoms.feed.bahmni.patient;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ContextConfiguration(classes = {OpenMRSPersonAttribute.class})
@ExtendWith(SpringExtension.class)
class OpenMRSPersonAttributeDiffblueTest {
    @Autowired
    private OpenMRSPersonAttribute openMRSPersonAttribute;

    /**
     * Method under test: {@link OpenMRSPersonAttribute#canEqual(Object)}
     */
    @Test
    void testCanEqual() {
        // Arrange, Act and Assert
        assertFalse(openMRSPersonAttribute.canEqual("Other"));
        assertTrue(openMRSPersonAttribute.canEqual(openMRSPersonAttribute));
    }

    /**
     * Method under test: {@link OpenMRSPersonAttribute#equals(Object)}
     */
    @Test
    void testEquals() {
        // Arrange, Act and Assert
        assertNotEquals(new OpenMRSPersonAttribute(), null);
        assertNotEquals(new OpenMRSPersonAttribute(), "Different type to OpenMRSPersonAttribute");
        assertNotEquals(new OpenMRSPersonAttribute(), 1);
    }

    /**
     * Method under test: {@link OpenMRSPersonAttribute#equals(Object)}
     */
    @Test
    void testEquals2() {
        // Arrange
        OpenMRSPersonAttribute openMRSPersonAttribute = new OpenMRSPersonAttribute();
        openMRSPersonAttribute.setUuid("01234567-89AB-CDEF-FEDC-BA9876543210");

        // Act and Assert
        assertNotEquals(openMRSPersonAttribute, new OpenMRSPersonAttribute());
    }

    /**
     * Method under test: {@link OpenMRSPersonAttribute#equals(Object)}
     */
    @Test
    void testEquals3() {
        // Arrange
        OpenMRSPersonAttribute openMRSPersonAttribute = new OpenMRSPersonAttribute();
        openMRSPersonAttribute.setVoided(true);

        // Act and Assert
        assertNotEquals(openMRSPersonAttribute, new OpenMRSPersonAttribute());
    }

    /**
     * Method under test: {@link OpenMRSPersonAttribute#equals(Object)}
     */
    @Test
    void testEquals4() {
        // Arrange
        OpenMRSPersonAttribute openMRSPersonAttribute = new OpenMRSPersonAttribute();
        openMRSPersonAttribute.setValue("Value");

        // Act and Assert
        assertNotEquals(openMRSPersonAttribute, new OpenMRSPersonAttribute());
    }

    /**
     * Method under test: {@link OpenMRSPersonAttribute#equals(Object)}
     */
    @Test
    void testEquals5() {
        // Arrange
        OpenMRSPersonAttribute openMRSPersonAttribute = new OpenMRSPersonAttribute();
        openMRSPersonAttribute.setAttributeType(new OpenMRSPersonAttributeType());

        // Act and Assert
        assertNotEquals(openMRSPersonAttribute, new OpenMRSPersonAttribute());
    }

    /**
     * Method under test: {@link OpenMRSPersonAttribute#equals(Object)}
     */
    @Test
    void testEquals6() {
        // Arrange
        OpenMRSPersonAttribute openMRSPersonAttribute = new OpenMRSPersonAttribute();

        OpenMRSPersonAttribute openMRSPersonAttribute2 = new OpenMRSPersonAttribute();
        openMRSPersonAttribute2.setUuid("01234567-89AB-CDEF-FEDC-BA9876543210");

        // Act and Assert
        assertNotEquals(openMRSPersonAttribute, openMRSPersonAttribute2);
    }

    /**
     * Method under test: {@link OpenMRSPersonAttribute#equals(Object)}
     */
    @Test
    void testEquals7() {
        // Arrange
        OpenMRSPersonAttribute openMRSPersonAttribute = new OpenMRSPersonAttribute();

        OpenMRSPersonAttribute openMRSPersonAttribute2 = new OpenMRSPersonAttribute();
        openMRSPersonAttribute2.setValue("Value");

        // Act and Assert
        assertNotEquals(openMRSPersonAttribute, openMRSPersonAttribute2);
    }

    /**
     * Method under test: {@link OpenMRSPersonAttribute#equals(Object)}
     */
    @Test
    void testEquals8() {
        // Arrange
        OpenMRSPersonAttribute openMRSPersonAttribute = new OpenMRSPersonAttribute();

        OpenMRSPersonAttribute openMRSPersonAttribute2 = new OpenMRSPersonAttribute();
        openMRSPersonAttribute2.setAttributeType(new OpenMRSPersonAttributeType());

        // Act and Assert
        assertNotEquals(openMRSPersonAttribute, openMRSPersonAttribute2);
    }

    /**
     * Method under test: {@link OpenMRSPersonAttribute#equals(Object)}
     */
    @Test
    void testEquals9() {
        // Arrange
        OpenMRSPersonAttribute openMRSPersonAttribute = new OpenMRSPersonAttribute();
        openMRSPersonAttribute.setValue(new OpenMRSPersonAttribute());

        // Act and Assert
        assertNotEquals(openMRSPersonAttribute, new OpenMRSPersonAttribute());
    }

    /**
     * Method under test: {@link OpenMRSPersonAttribute#equals(Object)}
     */
    @Test
    void testEquals10() {
        // Arrange
        OpenMRSPersonAttribute openMRSPersonAttribute = new OpenMRSPersonAttribute();
        openMRSPersonAttribute.setAttributeType(mock(OpenMRSPersonAttributeType.class));

        // Act and Assert
        assertNotEquals(openMRSPersonAttribute, new OpenMRSPersonAttribute());
    }

    /**
     * Methods under test:
     *
     * <ul>
     *   <li>{@link OpenMRSPersonAttribute#equals(Object)}
     *   <li>{@link OpenMRSPersonAttribute#hashCode()}
     * </ul>
     */
    @Test
    void testEqualsAndHashCode() {
        // Arrange
        OpenMRSPersonAttribute openMRSPersonAttribute = new OpenMRSPersonAttribute();

        // Act and Assert
        assertEquals(openMRSPersonAttribute, openMRSPersonAttribute);
        int expectedHashCodeResult = openMRSPersonAttribute.hashCode();
        assertEquals(expectedHashCodeResult, openMRSPersonAttribute.hashCode());
    }

    /**
     * Methods under test:
     *
     * <ul>
     *   <li>{@link OpenMRSPersonAttribute#equals(Object)}
     *   <li>{@link OpenMRSPersonAttribute#hashCode()}
     * </ul>
     */
    @Test
    void testEqualsAndHashCode2() {
        // Arrange
        OpenMRSPersonAttribute openMRSPersonAttribute = new OpenMRSPersonAttribute();
        OpenMRSPersonAttribute openMRSPersonAttribute2 = new OpenMRSPersonAttribute();

        // Act and Assert
        assertEquals(openMRSPersonAttribute, openMRSPersonAttribute2);
        int expectedHashCodeResult = openMRSPersonAttribute.hashCode();
        assertEquals(expectedHashCodeResult, openMRSPersonAttribute2.hashCode());
    }

    /**
     * Methods under test:
     *
     * <ul>
     *   <li>{@link OpenMRSPersonAttribute#equals(Object)}
     *   <li>{@link OpenMRSPersonAttribute#hashCode()}
     * </ul>
     */
    @Test
    void testEqualsAndHashCode3() {
        // Arrange
        OpenMRSPersonAttribute openMRSPersonAttribute = new OpenMRSPersonAttribute();
        openMRSPersonAttribute.setUuid("01234567-89AB-CDEF-FEDC-BA9876543210");

        OpenMRSPersonAttribute openMRSPersonAttribute2 = new OpenMRSPersonAttribute();
        openMRSPersonAttribute2.setUuid("01234567-89AB-CDEF-FEDC-BA9876543210");

        // Act and Assert
        assertEquals(openMRSPersonAttribute, openMRSPersonAttribute2);
        int expectedHashCodeResult = openMRSPersonAttribute.hashCode();
        assertEquals(expectedHashCodeResult, openMRSPersonAttribute2.hashCode());
    }

    /**
     * Methods under test:
     *
     * <ul>
     *   <li>{@link OpenMRSPersonAttribute#equals(Object)}
     *   <li>{@link OpenMRSPersonAttribute#hashCode()}
     * </ul>
     */
    @Test
    void testEqualsAndHashCode4() {
        // Arrange
        OpenMRSPersonAttribute openMRSPersonAttribute = new OpenMRSPersonAttribute();
        openMRSPersonAttribute.setValue("Value");

        OpenMRSPersonAttribute openMRSPersonAttribute2 = new OpenMRSPersonAttribute();
        openMRSPersonAttribute2.setValue("Value");

        // Act and Assert
        assertEquals(openMRSPersonAttribute, openMRSPersonAttribute2);
        int expectedHashCodeResult = openMRSPersonAttribute.hashCode();
        assertEquals(expectedHashCodeResult, openMRSPersonAttribute2.hashCode());
    }

    /**
     * Methods under test:
     *
     * <ul>
     *   <li>
     * {@link OpenMRSPersonAttribute#setAttributeType(OpenMRSPersonAttributeType)}
     *   <li>{@link OpenMRSPersonAttribute#setUuid(String)}
     *   <li>{@link OpenMRSPersonAttribute#setValue(Object)}
     *   <li>{@link OpenMRSPersonAttribute#setVoided(boolean)}
     *   <li>{@link OpenMRSPersonAttribute#toString()}
     *   <li>{@link OpenMRSPersonAttribute#getAttributeType()}
     *   <li>{@link OpenMRSPersonAttribute#getUuid()}
     *   <li>{@link OpenMRSPersonAttribute#getValue()}
     *   <li>{@link OpenMRSPersonAttribute#isVoided()}
     * </ul>
     */
    @Test
    void testGettersAndSetters() {
        // Arrange
        OpenMRSPersonAttribute openMRSPersonAttribute = new OpenMRSPersonAttribute();
        OpenMRSPersonAttributeType attributeType = new OpenMRSPersonAttributeType();

        // Act
        openMRSPersonAttribute.setAttributeType(attributeType);
        openMRSPersonAttribute.setUuid("01234567-89AB-CDEF-FEDC-BA9876543210");
        openMRSPersonAttribute.setValue("Value");
        openMRSPersonAttribute.setVoided(true);
        openMRSPersonAttribute.toString();
        OpenMRSPersonAttributeType actualAttributeType = openMRSPersonAttribute.getAttributeType();
        String actualUuid = openMRSPersonAttribute.getUuid();
        openMRSPersonAttribute.getValue();

        // Assert that nothing has changed
        assertEquals("01234567-89AB-CDEF-FEDC-BA9876543210", actualUuid);
        assertTrue(openMRSPersonAttribute.isVoided());
        assertSame(attributeType, actualAttributeType);
    }
}
