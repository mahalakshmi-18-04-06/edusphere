package com.example.edusphere.endpoint;

import java.io.StringReader;

import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;

import org.springframework.ws.context.MessageContext;
import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
import org.springframework.ws.server.endpoint.annotation.RequestPayload;
import org.springframework.ws.server.endpoint.annotation.ResponsePayload;
import org.springframework.ws.soap.SoapBody;
import org.springframework.ws.soap.SoapMessage;
import org.w3c.dom.Element;

import com.example.edusphere.entity.StudentResult;
import com.example.edusphere.service.StudentResultService;

@Endpoint
public class StudentResultEndpoint {

    private static final String NAMESPACE =
            "http://edusphere.com/student-results";

    private final StudentResultService studentResultService;

    public StudentResultEndpoint(StudentResultService studentResultService) {
        this.studentResultService = studentResultService;
    }

    @PayloadRoot(
            namespace = NAMESPACE,
            localPart = "GetStudentResultRequest"
    )
    @ResponsePayload
    public Source getStudentResult(
            @RequestPayload Element request,
            MessageContext messageContext) {

        String studentIdText = request
                .getElementsByTagNameNS(NAMESPACE, "studentId")
                .item(0)
                .getTextContent();

        Long studentId = Long.parseLong(studentIdText);

        try {

            StudentResult result =
                    studentResultService.getResultByStudentId(studentId);

            String response =
                    "<GetStudentResultResponse xmlns=\"" + NAMESPACE + "\">" +
                    "<studentId>" + result.getStudentId() + "</studentId>" +
                    "<studentName>" + result.getStudentName() + "</studentName>" +
                    "<semester>" + result.getSemester() + "</semester>" +
                    "<result>" + result.getResult() + "</result>" +
                    "<percentage>" + result.getPercentage() + "</percentage>" +
                    "</GetStudentResultResponse>";

            return new StreamSource(new StringReader(response));

        } catch (RuntimeException exception) {

            SoapMessage soapMessage =
                    (SoapMessage) messageContext.getResponse();

            SoapBody soapBody = soapMessage.getSoapBody();

            soapBody.addServerOrReceiverFault(
                    exception.getMessage(),
                    null
            );

            return null;
        }
    }
}