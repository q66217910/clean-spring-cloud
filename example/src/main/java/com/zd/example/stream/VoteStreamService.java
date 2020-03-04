package com.zd.example.stream;

import com.zd.feign.stream.VoteStream;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.Input;
import org.springframework.cloud.stream.annotation.Output;
import org.springframework.cloud.stream.messaging.Processor;
import reactor.core.publisher.Flux;

/**
 * 选举
 */
@EnableBinding(VoteStream.class)
public class VoteStreamService {

    @Output(VoteStream.OUTPUT)
    public Flux<String> vote(@Input(Processor.INPUT) Flux<String> input) {
        return input.map(String::toUpperCase);
    }

}
