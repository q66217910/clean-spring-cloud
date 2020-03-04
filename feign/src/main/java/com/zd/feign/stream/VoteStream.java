package com.zd.feign.stream;

import org.springframework.cloud.stream.annotation.Input;
import org.springframework.cloud.stream.annotation.Output;
import org.springframework.cloud.stream.messaging.Processor;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.SubscribableChannel;
import org.springframework.stereotype.Component;

@Component
public interface VoteStream extends Processor {

    /**
     * 选举
     */
    @Input
    SubscribableChannel receiveVote();

    @Output
    MessageChannel vote();

}
