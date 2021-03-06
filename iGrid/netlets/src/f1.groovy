#!/usr/bin/env nsh -f

import sorcer.arithmetic.provider.Adder;
import sorcer.arithmetic.provider.Multiplier;
import sorcer.arithmetic.provider.Subtractor;
import sorcer.service.*;

// Service Composition f1(f2(f4, f5), f3)

Task f4 = task("f4", sig("multiply", Multiplier.class),
		context("multiply", input("arg/x1", 10.0d), input("arg/x2", 50.0d),
		out("result/y1", null)));

Task f5 = task("f5", sig("add", Adder.class),
		context("add", input("arg/x3", 20.0d), input("arg/x4", 80.0d),
		output("result/y2", null)));

Task f3 = task("f3", sig("subtract", Subtractor.class),
		context("subtract", input("arg/x5", null), input("arg/x6", null),
		output("result/y3", null)));

//job("f1", job("f2", f4, f5), f3,
//job("f1", job("f2", f4, f5, strategy(Flow.PAR, Access.PULL)), f3,
Job f1 = job("f1", job("f2", f4, f5), f3, strategy(Provision.NO),
		pipe(out(f4, "result/y1"), input(f3, "arg/x5")),
		pipe(out(f5, "result/y2"), input(f3, "arg/x6")));
