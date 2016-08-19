package com.tcci.tccstore.servlet;

import java.io.File;
import java.io.IOException;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import cn.bluejoe.elfinder.controller.ConnectorController;
import cn.bluejoe.elfinder.controller.executor.CommandExecutor;
import cn.bluejoe.elfinder.controller.executor.CommandExecutorFactory;
import cn.bluejoe.elfinder.controller.executors.FileCommandExecutor;
import cn.bluejoe.elfinder.controller.executors.MissingCommandExecutor;
import cn.bluejoe.elfinder.impl.DefaultFsService;
import cn.bluejoe.elfinder.impl.DefaultFsServiceConfig;
import cn.bluejoe.elfinder.impl.FsSecurityCheckForAll;
import cn.bluejoe.elfinder.impl.StaticFsServiceFactory;
import cn.bluejoe.elfinder.localfs.LocalFsVolume;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import javax.annotation.Resource;
import javax.servlet.annotation.WebServlet;
import org.apache.commons.lang.StringUtils;

@WebServlet("/elfinder-servlet/connector")
public class ElfinderServlet extends HttpServlet implements CommandExecutorFactory {

    @Resource(mappedName = "jndi/tccstore.config")
    transient private Properties tccstoreConfig;

    private Map<String, CommandExecutor> _map = new HashMap<String, CommandExecutor>();
    private CommandExecutor _fallbackCommand;

    //core member of this Servlet
    ConnectorController _connectorController;

    /**
     * create a command executor factory
     *
     * @param config
     * @return
     */
    protected CommandExecutorFactory createCommandExecutorFactory(
            ServletConfig config) {
        // 僅允許 file command(顯示或下載)
        _map.put("file", new FileCommandExecutor());
        _fallbackCommand = new MissingCommandExecutor();
        return this;
    }

    /**
     * create a connector controller
     *
     * @param config
     * @return
     */
    protected ConnectorController createConnectorController(ServletConfig config) {
        ConnectorController connectorController = new ConnectorController();

        connectorController
                .setCommandExecutorFactory(createCommandExecutorFactory(config));
        connectorController.setFsServiceFactory(createServiceFactory(config));

        return connectorController;
    }

    protected DefaultFsService createFsService() {
        DefaultFsService fsService = new DefaultFsService();
        fsService.setSecurityChecker(new FsSecurityCheckForAll());

        DefaultFsServiceConfig serviceConfig = new DefaultFsServiceConfig();
        serviceConfig.setTmbWidth(80);

        fsService.setServiceConfig(serviceConfig);

        addVolumes(fsService);

        return fsService;
    }

    private LocalFsVolume createLocalFsVolume(String name, File rootDir) {
        LocalFsVolume localFsVolume = new LocalFsVolume();
        localFsVolume.setName(name);
        localFsVolume.setRootDir(rootDir);
        return localFsVolume;
    }

    private void addVolumes(DefaultFsService fsService) {
        String volumes = tccstoreConfig.getProperty("volumes");
        String[] ary = StringUtils.split(volumes, ";");
        if (ary != null && ary.length > 0) {
            for (String str : ary) {
                String[] ary2 = StringUtils.split(str, "|");
                if (ary2 != null && ary2.length == 3) {
                    LocalFsVolume fsVol = createLocalFsVolume(ary2[1], new File(ary2[2]));
                    fsService.addVolume(ary2[0], fsVol);
                }
            }
        }
    }

    /**
     * create a service factory
     *
     * @param config
     * @return
     */
    protected StaticFsServiceFactory createServiceFactory(ServletConfig config) {
        StaticFsServiceFactory staticFsServiceFactory = new StaticFsServiceFactory();
        DefaultFsService fsService = createFsService();

        staticFsServiceFactory.setFsService(fsService);
        return staticFsServiceFactory;
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        _connectorController.connector(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        _connectorController.connector(req, resp);
    }

    @Override
    public void init(ServletConfig config) throws ServletException {
        _connectorController = createConnectorController(config);
    }

    @Override
    public CommandExecutor get(String commandName) {
        if (_map.containsKey(commandName)) {
            return _map.get(commandName);
        }
        return _fallbackCommand;
    }

}
