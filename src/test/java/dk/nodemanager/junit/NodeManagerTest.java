package dk.nodemanager.junit;

import static org.assertj.core.api.Assertions.assertThat;

import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import dk.nodemanager.exception.NodeManagerException;
import dk.nodemanager.services.NodeServices;

@RunWith(SpringRunner.class)
@SpringBootTest
public class NodeManagerTest {

	@Autowired
	NodeServices nodeService;
	
	@Test
	public void test_get_all() {//Root node is always populated
		Assertions.assertThatCode(() -> nodeService.getAllNodes()).doesNotThrowAnyException();
		try {
			assertThat(nodeService.getAllNodes()).hasSize(1);
		} catch (NodeManagerException e) {
			e.printStackTrace();
		}
	}
	
}
