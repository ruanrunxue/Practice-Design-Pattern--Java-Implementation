package com.yrunz.designpattern.monitor.input;

import com.yrunz.designpattern.domain.Endpoint;
import com.yrunz.designpattern.monitor.config.json.JsonInputConfig;
import com.yrunz.designpattern.monitor.plugin.Event;
import com.yrunz.designpattern.network.Network;
import com.yrunz.designpattern.network.SocketData;
import org.junit.Test;

import static org.junit.Assert.*;

public class SocketInputTest {

    @Test
    public void testInstance() {
        String json = "{\"name\":\"socket_0\", \"type\":\"socket\", \"context\":{\"ip\":\"192.168.10.10\", \"port\":\"8080\"}}";
        JsonInputConfig config = JsonInputConfig.empty();
        config.load(json);
        assertEquals("socket_0", config.name());
        assertEquals(InputType.SOCKET, config.type());

        InputPlugin inputPlugin = InputPluginFactory.newInstance().create(config);
        assertNotNull(inputPlugin);

        assertTrue(inputPlugin instanceof SocketInput);
    }

    @Test
    public void testInput() {
        String json = "{\"name\":\"socket_0\", \"type\":\"socket\", \"context\":{\"ip\":\"192.168.10.10\", \"port\":\"8080\"}}";
        JsonInputConfig config = JsonInputConfig.empty();
        config.load(json);
        InputPlugin inputPlugin = InputPluginFactory.newInstance().create(config);
        inputPlugin.install();

        Endpoint src = Endpoint.ofDefaultPort("192.168.101.11");
        Endpoint dest = Endpoint.of("192.168.10.10", 8080);
        Network.instance().send(SocketData.of(src, dest, "hello world"));

        Event event = inputPlugin.input();
        assertEquals("192.168.101.11:80", event.header().get("peer"));
        assertEquals("hello world", event.payload());
    }

}