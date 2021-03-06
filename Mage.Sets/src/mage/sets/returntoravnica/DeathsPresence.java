/*
 *  Copyright 2010 BetaSteward_at_googlemail.com. All rights reserved.
 * 
 *  Redistribution and use in source and binary forms, with or without modification, are
 *  permitted provided that the following conditions are met:
 * 
 *     1. Redistributions of source code must retain the above copyright notice, this list of
 *        conditions and the following disclaimer.
 * 
 *     2. Redistributions in binary form must reproduce the above copyright notice, this list
 *        of conditions and the following disclaimer in the documentation and/or other materials
 *        provided with the distribution.
 * 
 *  THIS SOFTWARE IS PROVIDED BY BetaSteward_at_googlemail.com ``AS IS'' AND ANY EXPRESS OR IMPLIED
 *  WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND
 *  FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL BetaSteward_at_googlemail.com OR
 *  CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 *  CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 *  SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON
 *  ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 *  NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF
 *  ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 * 
 *  The views and conclusions contained in the software and documentation are those of the
 *  authors and should not be interpreted as representing official policies, either expressed
 *  or implied, of BetaSteward_at_googlemail.com.
 */

package mage.sets.returntoravnica;

import java.util.UUID;
import mage.abilities.TriggeredAbilityImpl;
import mage.abilities.effects.common.counter.AddCountersTargetEffect;
import mage.cards.CardImpl;
import mage.constants.CardType;
import mage.constants.Rarity;
import mage.constants.Zone;
import mage.counters.CounterType;
import mage.game.Game;
import mage.game.events.GameEvent;
import mage.game.events.GameEvent.EventType;
import mage.game.events.ZoneChangeEvent;
import mage.game.permanent.Permanent;
import mage.target.common.TargetControlledCreaturePermanent;

/**
 *
 * @author LevelX2
 */
public class DeathsPresence extends CardImpl {

    public DeathsPresence(UUID ownerId) {
        super(ownerId, 121, "Death's Presence", Rarity.RARE, new CardType[]{CardType.ENCHANTMENT}, "{5}{G}");
        this.expansionSetCode = "RTR";

        
        // Whenever a creature you control dies, put X +1/+1 counters on target creature you control, where X is the power of the creature that died.
        this.addAbility(new DeathsPresenceTriggeredAbility());
    }

    public DeathsPresence(final DeathsPresence card) {
        super(card);
    }

    @Override
    public DeathsPresence copy() {
        return new DeathsPresence(this);
    }
}

class DeathsPresenceTriggeredAbility extends TriggeredAbilityImpl {

    public DeathsPresenceTriggeredAbility() {
        super(Zone.BATTLEFIELD, null);
    }

    public DeathsPresenceTriggeredAbility(final DeathsPresenceTriggeredAbility ability) {
        super(ability);
    }

    @Override
    public DeathsPresenceTriggeredAbility copy() {
        return new DeathsPresenceTriggeredAbility(this);
    }

    @Override
    public boolean checkEventType(GameEvent event, Game game) {
        return event.getType() == EventType.ZONE_CHANGE;
    }

    @Override
    public boolean checkTrigger(GameEvent event, Game game) {
        if (((ZoneChangeEvent) event).getToZone() == Zone.GRAVEYARD
                && ((ZoneChangeEvent) event).getFromZone() == Zone.BATTLEFIELD) {
            Permanent permanent = (Permanent) game.getLastKnownInformation(event.getTargetId(), Zone.BATTLEFIELD);
            if (permanent != null && permanent.getControllerId().equals(this.getControllerId()) && permanent.getCardType().contains(CardType.CREATURE)) {
                this.getTargets().clear();
                this.addTarget(new TargetControlledCreaturePermanent());
                this.getEffects().clear();
                this.addEffect(new AddCountersTargetEffect(CounterType.P1P1.createInstance(permanent.getPower().getValue())));
                return true;
            }
        }
        return false;
    }

    @Override
    public String getRule() {
        return "Whenever a creature you control dies, put X +1/+1 counters on target creature you control, where X is the power of the creature that died.";
    }
}
