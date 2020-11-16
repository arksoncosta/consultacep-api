package br.com.arkson.consultacepapi.services;

import br.com.arkson.consultacepapi.model.Endereco;
import com.fasterxml.jackson.databind.exc.UnrecognizedPropertyException;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.xml.soap.*;
import java.io.ByteArrayOutputStream;
import java.util.Objects;
import java.util.regex.Pattern;

/**
 * @author arkson
 * @date 16/11/2020
 */
@Service
@Slf4j
public class ConsultaEnderecoServiceImpl implements ConsultaEnerecoService {

    private SOAPConnectionFactory soapConnectionFactory;
    private SOAPConnection soapConnection;

    @Value("${app.default-endpoint-url}")
    private String defaultEndpointUrl;
    @Value("${app.default-namespace}")
    protected String defaultNamespace;

    private static final Pattern PATTER_ONLY_NUM = Pattern.compile("\\d{8}");

    @PostConstruct
    public void setup() throws SOAPException {
        soapConnectionFactory = SOAPConnectionFactory.newInstance();
        soapConnection = soapConnectionFactory.createConnection();
    }

    @Override
    public Endereco getEnderecoCorreios(String cep) throws Exception {
        cep = Objects.requireNonNull(cep, "Cep inválido");

        if (!cep.matches(PATTER_ONLY_NUM.pattern())) {
            throw new IllegalArgumentException("Cep inválido");
        }

        SOAPMessage soapResponse = soapConnection.call(createSOAPRequest(cep), defaultEndpointUrl);

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        soapResponse.writeTo(out);
        String response = new String(out.toByteArray());
        log.info(response);
        response = response.replaceAll("\\<soap:[^>]*>", "");
        response = response.replaceAll("\\<ns2:[^>]*>", "");

        Endereco enderecoCorreios;
        try {
            XmlMapper xmlMapper = new XmlMapper();
            enderecoCorreios = xmlMapper.readValue(response, Endereco.class);
            out.close();
        } catch (UnrecognizedPropertyException e) {
            throw new Exception("Endereço não encontrado com o cep informado");
        }

        return enderecoCorreios;
    }

    protected SOAPMessage createSOAPRequest (String cep) throws Exception {
        MessageFactory messageFactory = MessageFactory.newInstance();
        SOAPMessage soapMessage = messageFactory.createMessage();

        this.createSOAPEnvelope(soapMessage, cep);
        MimeHeaders headers = soapMessage.getMimeHeaders();
        headers.addHeader("Content-Type", "text/xml; charset=utf-8");

        soapMessage.saveChanges();

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        soapMessage.writeTo(out);
        log.info(out.toString());

        return soapMessage;
    }

    protected void createSOAPEnvelope(SOAPMessage soapMessage, String cep) throws Exception {
        SOAPPart soapPart = soapMessage.getSOAPPart();

        String namespace = "myNamespace";
        String namespaceURI = defaultNamespace;

        SOAPEnvelope envelope = soapPart.getEnvelope();
        envelope.addNamespaceDeclaration(namespace, namespaceURI);


        SOAPBody body = envelope.getBody();
        SOAPElement principalElement =  body.addChildElement("consultaCEP", namespace);
        SOAPElement cepElement = principalElement.addChildElement("cep");
        cepElement.addTextNode(cep);

        principalElement.addChildElement(cepElement);
    }
}
