/*
* AMRIT - Accessible Medical Records via Integrated Technologies
* Integrated EHR (Electronic Health Records) Solution
*
* Copyright (C) "Piramal Swasthya Management and Research Institute"
*
* This file is part of AMRIT.
*
* This program is free software: you can redistribute it and/or modify
* it under the terms of the GNU General Public License as published by
* the Free Software Foundation, either version 3 of the License, or
* (at your option) any later version.
*
* This program is distributed in the hope that it will be useful,
* but WITHOUT ANY WARRANTY; without even the implied warranty of
* MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
* GNU General Public License for more details.
*
* You should have received a copy of the GNU General Public License
* along with this program.  If not, see https://www.gnu.org/licenses/.
*/
package com.wipro.fhir.utils.gateway.email;

import org.json.JSONException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mail.MailSendException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
@DisplayName("GenericEmailServiceImpl Test Suite")
class GenericEmailServiceImplTest {

    @Mock
    private JavaMailSender javaMailSender;

    private GenericEmailServiceImpl emailService;

    @BeforeEach
    @DisplayName("Wire the service with a mocked mail sender before each test")
    void setUp() {
        emailService = new GenericEmailServiceImpl();
        emailService.setJavaMailSender(javaMailSender);
    }

    private String request(String to) {
        return String.format(
                "{\"to\":\"%s\",\"from\":\"no-reply@amrit.example.org\","
                        + "\"subject\":\"Beneficiary ID pool low\","
                        + "\"message\":\"Only 100 IDs remain in the pool.\"}",
                to);
    }

    private SimpleMailMessage captureSentMessage() {
        ArgumentCaptor<SimpleMailMessage> captor = ArgumentCaptor.forClass(SimpleMailMessage.class);
        verify(javaMailSender).send(captor.capture());
        return captor.getValue();
    }

    @Nested
    @DisplayName("sendEmail without a template")
    class SendEmailTests {

        @Test
        @DisplayName("sendEmail should populate every field of the message from the JSON request")
        void sendEmail_shouldPopulateMessageFromJsonRequest() {
            emailService.sendEmail(request("ops@amrit.example.org"));

            SimpleMailMessage sent = captureSentMessage();
            assertArrayEquals(new String[] { "ops@amrit.example.org" }, sent.getTo());
            assertEquals("no-reply@amrit.example.org", sent.getFrom());
            assertEquals("Beneficiary ID pool low", sent.getSubject());
            assertEquals("Only 100 IDs remain in the pool.", sent.getText());
        }

        @Test
        @DisplayName("sendEmail should split a semicolon-separated recipient list into multiple addresses")
        void sendEmail_shouldSplitSemicolonSeparatedRecipients() {
            emailService.sendEmail(request("ops@amrit.example.org;admin@amrit.example.org"));

            assertArrayEquals(new String[] { "ops@amrit.example.org", "admin@amrit.example.org" },
                    captureSentMessage().getTo());
        }

        @Test
        @DisplayName("sendEmail should reject a request that is missing a mandatory field")
        void sendEmail_shouldRejectRequestMissingMandatoryField() {
            String incomplete = "{\"to\":\"ops@amrit.example.org\"}";

            assertThrows(JSONException.class, () -> emailService.sendEmail(incomplete));
            verify(javaMailSender, never()).send(org.mockito.ArgumentMatchers.any(SimpleMailMessage.class));
        }

        @Test
        @DisplayName("sendEmail should propagate a mail transport failure")
        void sendEmail_shouldPropagateTransportFailure() {
            doThrow(new MailSendException("smtp unreachable"))
                    .when(javaMailSender).send(org.mockito.ArgumentMatchers.any(SimpleMailMessage.class));

            assertThrows(MailSendException.class, () -> emailService.sendEmail(request("ops@amrit.example.org")));
        }
    }

    @Nested
    @DisplayName("sendEmail with a template")
    class SendEmailWithTemplateTests {

        @Test
        @DisplayName("sendEmail with a template should populate the message from the JSON request")
        void sendEmail_withTemplate_shouldPopulateMessageFromJsonRequest() {
            emailService.sendEmail(request("ops@amrit.example.org"), "pool-warning-template");

            SimpleMailMessage sent = captureSentMessage();
            assertArrayEquals(new String[] { "ops@amrit.example.org" }, sent.getTo());
            assertEquals("Beneficiary ID pool low", sent.getSubject());
            assertEquals("Only 100 IDs remain in the pool.", sent.getText());
        }

        @Test
        @DisplayName("sendEmail with a template should keep a semicolon list as a single recipient")
        void sendEmail_withTemplate_shouldKeepRecipientListUnsplit() {
            emailService.sendEmail(request("ops@amrit.example.org;admin@amrit.example.org"),
                    "pool-warning-template");

            assertArrayEquals(new String[] { "ops@amrit.example.org;admin@amrit.example.org" },
                    captureSentMessage().getTo());
        }
    }

    @Nested
    @DisplayName("sendEmailWithAttachment")
    class SendEmailWithAttachmentTests {

        @Test
        @DisplayName("sendEmailWithAttachment is not implemented and should send nothing")
        void sendEmailWithAttachment_shouldSendNothing() {
            emailService.sendEmailWithAttachment(request("ops@amrit.example.org"), "template");

            verify(javaMailSender, never()).send(org.mockito.ArgumentMatchers.any(SimpleMailMessage.class));
        }
    }
}
