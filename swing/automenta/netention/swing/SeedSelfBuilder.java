package automenta.netention.swing;

import automenta.netention.Mode;
import automenta.netention.Pattern;
import automenta.netention.impl.MemoryDetail;
import automenta.netention.impl.MemorySelf;
import automenta.netention.value.BoolProp;
import automenta.netention.value.IntProp;
import automenta.netention.value.NodeProp;
import automenta.netention.value.RealProp;
import automenta.netention.value.StringProp;
import automenta.netention.value.bool.BoolEquals;
import automenta.netention.value.bool.BoolIs;
import automenta.netention.value.integer.IntegerEquals;
import automenta.netention.value.integer.IntegerIs;
import automenta.netention.value.node.NodeEquals;
import automenta.netention.value.node.NodeIs;
import automenta.netention.value.real.RealEquals;
import automenta.netention.value.real.RealIs;
import automenta.netention.value.string.StringEquals;
import automenta.netention.value.string.StringIs;

public class SeedSelfBuilder {

    public SeedSelfBuilder() {
    }

    public void build(MemorySelf self) {
        self.addPattern(new Pattern("Built"));
        self.addPattern(new Pattern("Mobile"));
        self.addPattern(new Pattern("Person"));
        self.addPattern(new Pattern("Project"));
        self.addPattern(new Pattern("Event"));
        self.addPattern(new Pattern("Media"));
        self.addPattern(new Pattern("Message"));
        {
            self.addProperty(new IntProp("numWheels", "Number of Wheels"), "Built", "Mobile");
            self.addProperty(new StringProp("manufacturer", "Manufacturer"), "Built");
            self.addProperty(new RealProp("wheelRadius", "Wheel Radius"), "Mobile");
            self.addProperty(new NodeProp("anotherObject", "Another Object", "Built"));
            self.addProperty(new BoolProp("hasKickStand", "Has Kickstand"));
        }
        MemoryDetail d1 = new MemoryDetail("Real Bike", Mode.Real, "Built");
        MemoryDetail d2 = new MemoryDetail("Imaginary Bike", Mode.Imaginary, "Mobile", "Built");
        MemoryDetail d3 = new MemoryDetail("Empty Description", Mode.Real);
        {
            d1.addProperty("numWheels", new IntegerIs(4));
            d1.addProperty("manufacturer", new StringIs("myself"));
            d1.addProperty("wheelRadius", new RealIs(16.0));
            d1.addProperty("hasKickStand", new BoolIs(true));
            d1.addProperty("anotherObject", new NodeIs(d2.getID()));
        }
        {
            d2.addProperty("numWheels", new IntegerEquals(4));
            d2.addProperty("manufacturer", new StringEquals("myself"));
            d2.addProperty("wheelRadius", new RealEquals(16.0));
            d2.addProperty("hasKickStand", new BoolEquals(true));
            d2.addProperty("anotherObject", new NodeEquals(d1.getID()));
        }
        self.addDetail(d1);
        self.addDetail(d2);
        self.addDetail(d3);
    }
}
