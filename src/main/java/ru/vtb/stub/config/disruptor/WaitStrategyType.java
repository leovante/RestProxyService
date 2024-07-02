package ru.vtb.stub.config.disruptor;

import com.lmax.disruptor.*;

/**
 * Defines Ring buffer Wait strategies and initializes them. 
 * 
 * @author Anoop Nair
 *
 */
public enum WaitStrategyType {
        /**
         * @see BlockingWaitStrategy
         */
        BLOCKING {
            public WaitStrategy instance() {
                return new BlockingWaitStrategy();
            }
        },

        /**
         * @see BusySpinWaitStrategy
         */
        BUSY_SPIN {
            public WaitStrategy instance() {
                return new BusySpinWaitStrategy();
            }
        },

        /**
         * @see LiteBlockingWaitStrategy
         */
        LITE_BLOCKING {
            public WaitStrategy instance() {
                return new LiteBlockingWaitStrategy();
            }
        },
        
        /**
         * @see SleepingWaitStrategy
         */
        SLEEPING_WAIT {
            public WaitStrategy instance() {
                return new SleepingWaitStrategy();
            }
        },
        
        /**
         * @see YieldingWaitStrategy
         */
        YIELDING {
            public WaitStrategy instance() {
                return new YieldingWaitStrategy();
            }
        };
        
        abstract WaitStrategy instance();
}
