/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mercury.infra.bus.monitor;

/**
 *
 * @author maskimko
 */
public interface MonitorBusJMX {

    public void stopCollectingStatistics();

    public java.lang.String showDurableSubscribersInfo();

    public void enableMonitors();

    public void disableMonitors();

    public java.lang.String showConnectionDetailsMonitor();

    public java.lang.String showConnectionDetails();

    public java.lang.String showQueueInfoMonitor();

    public java.lang.String showGroupBrokerMetricInfoMonitor();

    public void reloadMonitors();

    public void start();

    public java.lang.String showGroupContainerMetricInfoMonitor();

    public void startCollectingStatistics();

    public java.lang.String showGroupSubscriberMetricInfoMonitor();

    public java.lang.String showQueueInfo();

    public java.lang.String showFlowToDiskMonitor();

    public java.lang.String showMetricsInfo();

    public java.lang.String showGroupDurableSubscriberInfoMonitor();

}
