// Copyright © 2012-2020 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.lattice.grid;

import io.vlingo.actors.Returns;
import io.vlingo.cluster.model.application.ClusterApplicationAdapter;
import io.vlingo.cluster.model.attribute.Attribute;
import io.vlingo.cluster.model.attribute.AttributesProtocol;
import io.vlingo.lattice.grid.application.ApplicationMessageHandler;
import io.vlingo.lattice.grid.application.GridActorControl;
import io.vlingo.lattice.grid.application.GridActorControlMessageHandler;
import io.vlingo.lattice.grid.application.message.Answer;
import io.vlingo.lattice.grid.application.message.Deliver;
import io.vlingo.lattice.grid.application.message.Start;
import io.vlingo.wire.fdx.outbound.ApplicationOutboundStream;
import io.vlingo.wire.message.RawMessage;
import io.vlingo.wire.node.Id;
import io.vlingo.wire.node.Node;

import java.util.Collection;

public class GridNode extends ClusterApplicationAdapter {
  private AttributesProtocol client;
  private final Grid grid;
  private final Node localNode;
  private ApplicationOutboundStream responder;

  final ApplicationMessageHandler applicationMessageHandler;

  public GridNode(final Grid grid, final Node localNode) {
    this.grid = grid;
    this.localNode = localNode;
    this.applicationMessageHandler = new GridActorControlMessageHandler(new GridActorControl() {
      @Override
      public void start(Start start) {
        logger().debug("GRID: Received application message: Start");
      }

      @Override
      public void deliver(Deliver deliver) {
        logger().debug("GRID: Received application message: Deliver");
      }

      @Override
      public void answer(Answer answer) {
        logger().debug("GRID: Received application message: Answer");
      }

      @Override
      public <T> void completeWithAnswer(Returns<T> returns) { }
    });
  }

  @Override
  public void start() {
    logger().debug("GRID: Started on node: " + localNode);
    grid.hashRing().includeNode(localNode.id());
  }

  @Override
  public void handleApplicationMessage(final RawMessage message) {
    logger().debug("GRID: Received application message: " + message.asTextMessage());
    applicationMessageHandler.handle(message);
  }

  @Override
  public void informAllLiveNodes(final Collection<Node> liveNodes, final boolean isHealthyCluster) {
    logger().debug("GRID: Live nodes confirmed: " + liveNodes + " and is healthy: " + isHealthyCluster);
  }

  @Override
  public void informLeaderElected(final Id leaderId, final boolean isHealthyCluster, final boolean isLocalNodeLeading) {
    logger().debug("GRID: Leader elected: " + leaderId + " and is healthy: " + isHealthyCluster);

    if (isLocalNodeLeading) {
      logger().debug("GRID: Local node is leading.");
   }
  }

  @Override
  public void informLeaderLost(final Id lostLeaderId, final boolean isHealthyCluster) {
    logger().debug("GRID: Leader lost: " + lostLeaderId + " and is healthy: " + isHealthyCluster);
  }

  @Override
  public void informLocalNodeShutDown(final Id nodeId) {
    logger().debug("GRID: Local node shut down: " + nodeId);
  }

  @Override
  public void informLocalNodeStarted(final Id nodeId) {
    logger().debug("GRID: Local node started: " + nodeId);
  }

  @Override
  public void informNodeIsHealthy(final Id nodeId, final boolean isHealthyCluster) {
    logger().debug("GRID: Node reported healthy: " + nodeId + " and is healthy: " + isHealthyCluster);
  }

  @Override
  public void informNodeJoinedCluster(final Id nodeId, final boolean isHealthyCluster) {
    logger().debug("GRID: Node joined: " + nodeId + " and is healthy: " + isHealthyCluster);
  }

  @Override
  public void informNodeLeftCluster(final Id nodeId, final boolean isHealthyCluster) {
    logger().debug("GRID: Node left: " + nodeId + " and is healthy: " + isHealthyCluster);
  }

  @Override
  public void informQuorumAchieved() {
    logger().debug("GRID: Quorum achieved");
  }

  @Override
  public void informQuorumLost() {
    logger().debug("GRID: Quorum lost");
  }

  @Override
  public void informResponder(final ApplicationOutboundStream responder) {
    this.responder = responder;
    logger().debug("GRID: Informed of responder: " + this.responder);
  }

  @Override
  public void informAttributesClient(final AttributesProtocol client) {
    logger().debug("GRID: Attributes Client received.");
    this.client = client;
  }

  @Override
  public void informAttributeSetCreated(final String attributeSetName) {
     logger().debug("GRID: Attributes Set Created: " + attributeSetName);
  }

  @Override
  public void informAttributeAdded(final String attributeSetName, final String attributeName) {
    final Attribute<String> attr = client.attribute(attributeSetName, attributeName);
    logger().debug("GRID: Attribute Set " + attributeSetName + " Attribute Added: " + attributeName + " Value: " + attr.value);
  }

  @Override
  public void informAttributeRemoved(final String attributeSetName, final String attributeName) {
    final Attribute<String> attr = client.attribute(attributeSetName, attributeName);
    logger().debug("GRID: Attribute Set " + attributeSetName + " Attribute Removed: " + attributeName + " Attribute: " + attr);
  }

  @Override
  public void informAttributeSetRemoved(final String attributeSetName) {
    logger().debug("GRID: Attributes Set Removed: " + attributeSetName);
  }

  @Override
  public void informAttributeReplaced(final String attributeSetName, final String attributeName) {
    final Attribute<String> attr = client.attribute(attributeSetName, attributeName);
    logger().debug("GRID: Attribute Set " + attributeSetName + " Attribute Replaced: " + attributeName + " Value: " + attr.value);
  }
}
