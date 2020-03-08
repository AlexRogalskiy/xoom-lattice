// Copyright © 2012-2020 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.lattice.model;

/**
 * Provides the means to request the identity of the {@code DomainEvent}.
 */
public abstract class IdentifiedDomainEvent extends DomainEvent {
  /**
   * Construct my default state with a type version of 1.
   */
  public IdentifiedDomainEvent() {
    super();
  }

  /**
   * Construct my default state with a {@code eventTypeVersion} greater than 1.
   * @param eventTypeVersion the int version of this event type
   */
  public IdentifiedDomainEvent(final int eventTypeVersion) {
    super(eventTypeVersion);
  }

  /**
   * Answer the {@code String} identity of this {@code DomainEvent}.
   * @return String
   */
  public abstract String identity();
}
