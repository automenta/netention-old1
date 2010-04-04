package automenta.netention.swing;

import automenta.netention.Mode;
import automenta.netention.Pattern;
import automenta.netention.impl.MemoryDetail;
import automenta.netention.impl.MemorySelf;
import automenta.netention.value.BoolProp;
import automenta.netention.value.IntProp;
import automenta.netention.value.RealProp;
import automenta.netention.value.StringProp;
import automenta.netention.value.bool.BoolEquals;
import automenta.netention.value.bool.BoolIs;
import automenta.netention.value.integer.IntegerEquals;
import automenta.netention.value.integer.IntegerIs;
import automenta.netention.value.real.RealEquals;
import automenta.netention.value.real.RealIs;
import automenta.netention.value.string.StringEquals;
import automenta.netention.value.string.StringIs;

public class SeedSelfBuilder {

    public SeedSelfBuilder() {
    }

    public void build(MemorySelf s) {
        s.addPattern(new Pattern("Built").
                        setIconURL("media://tango32/categories/preferences-system.png").
                        setDescription("Something that is built or manufactured"));
        {
            s.addProperties("Built",
                    new StringProp("manufacturer", "Manufacturer"),
                    new StringProp("serialNumber", "Serial Number"),
                    new /*TimePoint*/StringProp("builtWhen", "When Built"));
        }

        s.addPattern(new Pattern("Mobile").setIconURL("media://tango32/places/start-here.png"));
        {
            s.addProperties("Mobile",
                    new IntProp("numWheels", "Number of Wheels"),
                    new RealProp("wheelRadius", "Wheel Radius"),
                    new BoolProp("hasKickStand", "Has Kickstand"));
        }

        s.addPattern(new Pattern("Person").setIconURL("media://tango32/apps/system-users.png"));
        {
            s.addProperties("Person",
                new StringProp("fullName", "Full Name"),
                new StringProp("biography", "Biography"),
                new StringProp("emailAddress", "E-Mail"),
                new StringProp("webAddress", "Website"),
                new StringProp("birthdate", "Birthdate")
                );
        }

        s.addPattern(new Pattern("Project").setIconURL("media://tango32/mimetypes/x-office-presentation.png"));
        {

        }

        s.addPattern(new Pattern("Event").setIconURL("media://tango32/mimetypes/x-office-calendar.png"));
        {

        }

        s.addPattern(new Pattern("Media").setIconURL("media://tango32/categories/applications-multimedia.png"));
        {

        }

        s.addPattern(new Pattern("Message").setIconURL("media://tango32/apps/internet-mail.png"));
        {
            s.addProperty(new StringProp("recipient", "Recipient"), "Message");
        }

        



        MemoryDetail d1 = new MemoryDetail("Red Bike", Mode.Real, "Built", "Mobile");
        MemoryDetail d11 = new MemoryDetail("Blue Bike", Mode.Real, "Built");
        MemoryDetail d2 = new MemoryDetail("Imaginary Bike", Mode.Imaginary, "Mobile", "Built");
        MemoryDetail d3 = new MemoryDetail("Empty Description", Mode.Real);
        {
            d1.addProperty("numWheels", new IntegerIs(4));
            d1.addProperty("manufacturer", new StringIs("myself"));
            d1.addProperty("wheelRadius", new RealIs(16.0));
            d1.addProperty("hasKickStand", new BoolIs(true));
            //d1.addProperty("anotherObject", new NodeIs(d2.getID()));
            {
                d11.addProperty("numWheels", new IntegerIs(2));
                d11.addProperty("manufacturer", new StringIs("myself"));
                d11.addProperty("wheelRadius", new RealIs(16.0));
                d11.addProperty("hasKickStand", new BoolIs(true));
                //d11.addProperty("anotherObject", new NodeIs(d2.getID()));
            }

        }
        {
            d2.addProperty("numWheels", new IntegerEquals(4));
            d2.addProperty("manufacturer", new StringEquals("myself"));
            d2.addProperty("wheelRadius", new RealEquals(16.0));
            d2.addProperty("hasKickStand", new BoolEquals(true));
            //d2.addProperty("anotherObject", new NodeEquals(d1.getID()));
        }
        s.addDetail(d1);
        s.addDetail(d11);
        s.addDetail(d2);
        s.addDetail(d3);
    }
}

//<schema>
//
//	<pattern id="Owned" name="Owned">
//		Something that is or can be owned, and possibly change ownership
//		<node id="owner" class="Being"/>
//		<node id="ownerNext" name="Next Owner" class="Being"/>
//	</pattern>
//
//	<pattern id="Valued" name="Worth Something">
//		Value of something; how much its worth
//	</pattern>
//
//	<pattern id="DollarValued" name="Worth US Dollars ($)">
//		Dollar value
//        <extend id="Valued"/>
//	</pattern>
//
//	<pattern id="ThingValued" name="Worth the Same Value as...">
//		Has a value that is the same as other objects
//        <node id="valuedSimilarlyTo" name="Valued Similarly To" maxMult="-1"/>
//        <extend id="Valued"/>
//	</pattern>
//
//	<pattern id="Rented" name="Rentable">
//		Something that is or can be rented/loaned/leased/borrowed
//        <extend id="Owned"/>
//		<node id="renter" class="Being"/>
//		<node id="renterNext" name="Next Renter" class="Being"/>
//		<real id="dailyCost" name="Daily Cost" unit="currency"/>
//	</pattern>
//
//	<pattern id="Located">
//		Something that has a specific location
//		<geopoint id="location"/>
//	</pattern>
//
//	<pattern id="Mobile">
//		Something that has a specific location, and a possible next location
//		<extend id="Located"/>
//		<geopoint id="locationNext" name="Next Location"/>
//	</pattern>
//
//	<pattern id="Delivered">
//		<extend id="Mobile"/>
//	</pattern>
//
//    <pattern id="Service">
//    </pattern>
//
//	<pattern id="Event">
//		<timepoint id="startTime" name="Start Time"/>
//		<timepoint id="endTime" name="End Time"/>
//	</pattern>
//
//    <pattern id="Reservation">
//        A reservation, ticket, or other reservable activity.  Ex: Restaurants, Shows, etc...
//        <extend id="Event"/>
//    </pattern>
//
//    <pattern id="RestaurantVisit" name="Restaurant Visit">
//            <extend id="Reservation"/>
//    </pattern>
//
//    <pattern id="MovieShowing" name="Movie Showing">
//            <extend id="Reservation"/>
//    </pattern>
//
//    <pattern id="Carpool" name="Carpool">
//            <extend id="Event"/>
//            <extend id="Mobile"/>
//    </pattern>
//
//
//    <pattern id="Gig">
//        Job with fixed start and stop time.
//        <extend id="Event"/>
//        <extend id="Service"/>
//    </pattern>
//
//    <pattern id="Project">
//        Project or goal
//    </pattern>
//
//	<pattern id="PhysicalThing" name="Physical Object">
//		<string id="color" maxMult="-1">
//			Predominant exterior color
//		</string>
//
//		<string id="material" maxMult="-1">
//			Predominant exterior material
//		</string>
//
//		<string id="condition" maxMult="1">
//			Old or new
//			<values>old, new</values>
//		</string>
//
//		<real id="mass" unit="mass"/>
//		<real id="length" unit="distance"/>
//		<real id="width" unit="distance"/>
//		<real id="height" unit="distance"/>
//
//	</pattern>
//
//	<pattern id="Built">
//		Something that is built or manufactured
//		<string id="builder" name="Builder Name"/>
//		<string id="serialID" name="Serial Number"/>
//		<timepoint id="builtWhen" name="When Built"/>
//	</pattern>
//
//	<pattern id="Being">
//		Life form
//	</pattern>
//
//    <pattern id="Animal">
//		<extend id="Being"/>
//    </pattern>
//
//	<pattern id="Human">
//		Human being
//		<extend id="Being"/>
//		<string id="firstName" name="First Name"/>
//		<string id="lastName" name="Last Name"/>
//		<string id="biography"/> <!-- richtext -->
//		<string id="emailAddress" name="EMail Address"/>
//		<string id="webURL" name="Website"/>
//	</pattern>
//
//	<pattern id="Bicycle">
//		<extend id="Built"/>
//		<int id="gearCount" name="Gear Count"/>
//		<real id="wheelDiameter" name="Wheel Diameter" unit="distance"/>
//		<string id="bicycleType" name="Bicycle Type">
//			[ 'mountain',
//       			"street",
//       			"hybrid",
//       			"tricycle",
//       			"unicycle",
//       			"tandem",
//       			"recumbent"
//       		]);
//		</string>
//	</pattern>
//
//	<pattern id="Dwelling">
//		<extend id="Built"/>
//		<int id="numBedrooms" name="Number of Bedrooms"/>
//	</pattern>
//
//	<pattern id="Guitar">
//		<extend id="Built"/>
//    </pattern>
//
//	<pattern id="OnlineAccount" name="Online Account">
//        <string id="login" name="Login" minMult="1" maxMult="1"/>
//        <string type="password" id="password" name="Password" minMult="1" maxMult="1"/>
//    </pattern>
//
//    <pattern id="TwitterAccount" name="Twitter Account">
//        Twitter Account
//        <extend id="OnlineAccount"/>
//    </pattern>
//
//    <pattern id="FaceBookAccount" name="FaceBook Account">
//        FaceBook Account
//        <extend id="OnlineAccount"/>
//    </pattern>
//
//    <pattern id="GoogleAccount" name="Google Account">
//        Google Account
//        <extend id="OnlineAccount"/>
//    </pattern>
//
//
//    <pattern id="MotorVehicle" name="Motor Vehicle">
//        <extend id="Built"/>
//    </pattern>
//
//    <pattern id="Test">Test</pattern>
//
//	<pattern id="testReal" name="Real Test">
//		<extend id="test"/>
//		<real id="rn1"/>
//		<real id="rn2"/>
//		<real id="rn3"/>
//		<real id="rn4"/>
//	</pattern>
//
//	<pattern id="testString" name="String Test">
//		<extend id="test"/>
//		<string id="s1"/>
//		<string id="s2"/>
//		<string id="s3"/>
//		<string id="s4"/>
//	</pattern>
//
//	<pattern id="testInteger" name="Integer Test">
//		<extend id="test"/>
//		<int id="i1"/>
//		<int id="i2"/>
//		<int id="i3"/>
//		<int id="i4"/>
//	</pattern>
//
//	<pattern id="testNode" name="Node Test">
//		<extend id="test"/>
//		<node id="n1"/>
//		<node id="n2"/>
//		<node id="n3"/>
//		<node id="n4"/>
//	</pattern>
//
//
//
//</schema>
//