package at.ac.uibk.fiba.arunda.webapp.web;

import at.ac.uibk.fiba.arunda.webapp.application.Application;
import org.apache.commons.io.FileUtils;
import org.junit.Assert;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.servlet.http.HttpServletResponse;
import java.io.File;

/**
 * Created by joseph on 10/25/16.
 */
@ContextConfiguration(classes = {Application.class})
@RunWith(SpringJUnit4ClassRunner.class)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class WebEndpointTest {

    @Autowired
    private WebEndpoint endpoint;

    @Test
    public void test_stamping() throws Exception {
        File f = new File("src/test/resources/01_(7).pdf");
        byte[] content = FileUtils.readFileToByteArray(f);
        MockMultipartFile file = new MockMultipartFile(f.getName(), content);
        MockHttpServletResponse resp = new MockHttpServletResponse();
        endpoint.stamp(file, resp);
        Assert.assertEquals(HttpServletResponse.SC_OK, resp.getStatus());
    }

    @Test
    public void test_converting() throws Exception {
        File f = new File("src/test/resources/arunda.odb");
        byte[] content = FileUtils.readFileToByteArray(f);
        MockMultipartFile file = new MockMultipartFile(f.getName(), content);
        MockHttpServletResponse resp = new MockHttpServletResponse();
        endpoint.convert(file, resp);
        Assert.assertEquals(HttpServletResponse.SC_OK, resp.getStatus());
        System.out.println(resp.getContentAsString());
    }

    @Test
    public void test_thumbing() throws Exception {
        File f = new File("src/test/resources/01_(7).pdf");
        byte[] content = FileUtils.readFileToByteArray(f);
        MockMultipartFile file = new MockMultipartFile(f.getName(), content);
        MockHttpServletResponse resp = new MockHttpServletResponse();
        endpoint.thumb(file, resp);
        Assert.assertEquals(HttpServletResponse.SC_OK, resp.getStatus());
    }

}
