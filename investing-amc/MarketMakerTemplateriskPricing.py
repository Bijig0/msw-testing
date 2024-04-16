from datamodel import OrderDepth, UserId, TradingState, Order
from typing import List
import string
import jsonpickle
import math


class Trader:
    def run(self, state: TradingState):
        ##DEFINE VARIABLES
        data = jsonpickle.decode(state.traderData) if len(state.traderData) > 0 else {}

        starfruit_price_cache = data["starfruit_price_cache"] if "starfruit_price_cache" in data else []
        
        result = {}
        print(state.own_trades)
        print(state.market_trades)

        bucket_limits = {"AMETHYSTS": 20, "STARFRUIT": 20}
        
        for product in state.order_depths:
            if product not in state.position.keys():
                state.position[product] = 0
            total_qty_bid = 0
            total_qty_offered = 0 ##A negative number or zero.

            ##We have these because if the sum of the quantities of all our bids + our current position exceeds
            ##the position limit, all orders will be canceled.
            bid_qty_limit = bucket_limits[product] - state.position[product]
            offer_qty_limit = -bucket_limits[product] - state.position[product]

            orders: List[Order] = [] ##Output variable containing the orders we will make.
            order_depth: OrderDepth = state.order_depths[product] ##The order book.
            post_arb_position = state.position[product] ##This variable will track the position of our warehouse after aggressive trades.
            ## DECLARE MAIN PARAMETERS. THESE VALUES ARE MEANINGLESS. (How do I declare variables without initalising in python?)
            half_spread = 2 
            risk_exit_threshhold = 0.5
            behind_threshhold = 1
            fair_price = 10000
            our_bid = 9995
            our_ask = 10005
            """
            Now we construct pricing.
            """
            if product == "AMETHYSTS":
                ##MAIN PARAMETERS
                half_spread = 2 
                risk_exit_threshhold = 0
                behind_threshhold = 1
                fair_price = 10000
                our_bid = 9995
                our_ask = 10005
            elif product == "STARFRUIT":
                """
                TODO: 
                i) Better Starfruit pricing
                ii) Model what happens to the book after we send aggressive orders.
                """
                market_primary_ask = 10000000
                market_primary_bid = 0
                market_primary_ask_volume = 0
                market_primary_bid_volume = 0
                
                if len(order_depth.buy_orders) != 0:
                    for bid, bid_amount in list(order_depth.buy_orders.items()):
                        if bid_amount > market_primary_bid_volume:
                            market_primary_bid_volume = bid_amount
                            market_primary_bid = bid
                if len(order_depth.sell_orders) != 0:
                    for ask, ask_amount in list(order_depth.sell_orders.items()):
                        if ask_amount < market_primary_ask_volume:
                            market_primary_ask_volume = ask_amount
                            market_primary_ask = ask
                current_primary_mid = (market_primary_bid + market_primary_ask) / 2
                
                if len(starfruit_price_cache) < 10:
                    starfruit_price_cache.append(current_primary_mid)
                else:
                    starfruit_price_cache.pop(0)

                """
                PRICE:
                diff1   -0.263826
                diff2   -0.064626
                diff3   -0.025465
                diff5    0.012440
                dtype: float64
                """
                diff0 = 0
                diff1 = 0
                diff2 = 0
                diff3 = 0
                diff5 = 0
                if len(starfruit_price_cache) >= 6:
                    diff1 = starfruit_price_cache[len(starfruit_price_cache)-1] - starfruit_price_cache[len(starfruit_price_cache)-2]
                    diff2 = starfruit_price_cache[len(starfruit_price_cache)-2] - starfruit_price_cache[len(starfruit_price_cache)-3]
                    diff3 = starfruit_price_cache[len(starfruit_price_cache)-3] - starfruit_price_cache[len(starfruit_price_cache)-4]
                    diff5 = starfruit_price_cache[len(starfruit_price_cache)-5] - starfruit_price_cache[len(starfruit_price_cache)-6]
                    diff0 = -0.263826 * diff1 -0.064626 * diff2 -0.025465 * diff3 + 0.012440 * diff5
                ##MAIN PARAMETERS
                half_spread = 1.4
                risk_exit_threshhold = 1.6
                behind_threshhold = 1
                fair_price = current_primary_mid + diff0
                our_bid = market_primary_bid - 1
                our_ask = market_primary_ask + 1

            
            # Now we begin the main market making code:
            
            
            if len(order_depth.buy_orders) != 0:
                cumil_bid_amt_2_or_wider = 0
                for bid, bid_amount in list(order_depth.buy_orders.items()):
                    if bid > fair_price:
                        qty_to_trade = max(offer_qty_limit - total_qty_offered, -bid_amount)
                        orders.append(Order(product, bid, qty_to_trade))
                        total_qty_offered += qty_to_trade
                        post_arb_position += qty_to_trade
                    elif abs(bid - fair_price) <= risk_exit_threshhold and post_arb_position > 5:
                        # This code makes any position reducing trade that is within risk_exit_threshhold 
                        # of theo/mid.
                        qty_to_trade = max(-post_arb_position, -bid_amount)
                        orders.append(Order(product, bid, qty_to_trade))
                        total_qty_offered += qty_to_trade
                        post_arb_position += qty_to_trade
                        #print(f"Risk Exit for Free for {qty_to_trade} units of {product}")
                    elif bid <= fair_price - half_spread:
                        # This code makes sure we beat every sufficiently wide price. 
                        # We won't be trying to compete with some desparate fool who quotes at mid.
                        cumil_bid_amt_2_or_wider += bid_amount
                        if cumil_bid_amt_2_or_wider >= behind_threshhold:
                            our_bid = max(our_bid, bid + 1)
            if len(order_depth.sell_orders) != 0:
                cumil_ask_amt_2_or_wider = 0
                for ask, ask_amount in list(order_depth.sell_orders.items()):
                    if ask < fair_price:
                        qty_to_trade = min(bid_qty_limit - total_qty_bid, -ask_amount)
                        orders.append(Order(product, ask, qty_to_trade))
                        total_qty_bid += qty_to_trade
                        post_arb_position += qty_to_trade
                    elif abs(ask - fair_price) <= risk_exit_threshhold and post_arb_position < -5:
                        # This code makes any position reducing trade that is within risk_exit_threshhold 
                        # of theo/mid.
                        qty_to_trade = min(-post_arb_position, -ask_amount)
                        orders.append(Order(product, ask, qty_to_trade))
                        total_qty_bid += qty_to_trade
                        post_arb_position += qty_to_trade
                        #print(f"Risk Exit for Free for {qty_to_trade} units of {product}")
                    elif ask >= fair_price + half_spread:
                        # This code makes sure we beat every sufficiently wide price. 
                        # We won't be trying to compete with some desparate fool who quotes at mid.
                        cumil_ask_amt_2_or_wider += ask_amount
                        if cumil_ask_amt_2_or_wider <= -behind_threshhold:
                            our_ask = min(our_ask, ask - 1)

            #assert(total_qty_bid * total_qty_offered == 0)
            
            
            print("Our position is: "+str(state.position))
            print("arbitraged: "+str(total_qty_bid + total_qty_offered))
            assert(total_qty_bid <= bid_qty_limit)
            assert(total_qty_offered >= offer_qty_limit)
            
            # Now we send our passive orders. I have not attempted to insert quotes at separate layers of the book as 
            # the trade sizes for AMETHYSTS and STARFRUIT are too small to justify that.
            """
            Price Adjustment when we have large positions.
            """
            """
            sta_adjustment = 1 if product == "STARFRUIT" else 0
            
            if post_arb_position > 18:
                our_ask = max(math.ceil(fair_price + sta_adjustment), our_ask - 1)
                our_bid = min(math.floor(fair_price - sta_adjustment), our_bid - 1)
            if post_arb_position < -18:
                our_ask = max(math.ceil(fair_price + sta_adjustment), our_ask + 1)
                our_bid = min(math.floor(fair_price - sta_adjustment), our_bid + 1)
            """
            
            volume_bid = bid_qty_limit - total_qty_bid
            volume_ask = offer_qty_limit - total_qty_offered
            
            if volume_bid > 0:
                orders.append(Order(product, our_bid, volume_bid))
                total_qty_bid += volume_bid

            if volume_ask < 0:
                orders.append(Order(product, our_ask, volume_ask))
                total_qty_offered += volume_ask
                
            assert(total_qty_offered <= 0)
            assert(total_qty_offered >= offer_qty_limit)

            assert(total_qty_bid >= 0)
            assert(total_qty_bid <= bid_qty_limit)
            
            result[product] = orders
            
        ##SAVES DATA
        data["starfruit_price_cache"] = starfruit_price_cache
        
        print(f'the time is {state.timestamp}', end = ' ')
        traderData = jsonpickle.encode(data)
        conversions = 1
        print(" our final orders are: ")
        print(result)
        return result, conversions, traderData