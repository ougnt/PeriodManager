package com.ougnt.period_manager.activity.extra;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.LinkedList;
import java.util.List;


public class NewInstructionActivityExtra {
    private static final String Components = "Component";
    private static final String StartX = "StartX";
    private static final String StartY = "StartY";
    private static final String Width = "Width";
    private static final String Height = "Height";
    private static final String Text = "Text";

    public LinkedList<InstructionComponent> components;

    private NewInstructionActivityExtra() {
        components = new LinkedList<>();
    }

    public static NewInstructionActivityExtra fromJsonString(String jsonExtra) {
        NewInstructionActivityExtra ret = new NewInstructionActivityExtra();

        try {
            JSONObject jsonObj = new JSONObject(jsonExtra);
            JSONArray coms = jsonObj.getJSONArray(Components);
            for (int i = 0; i < coms.length(); i++) {
                JSONObject tempObj = coms.getJSONObject(i);
                InstructionComponent component = new InstructionComponent(
                        tempObj.getInt(StartX),
                        tempObj.getInt(StartY),
                        tempObj.getInt(Width),
                        tempObj.getInt(Height),
                        tempObj.getString(Text));

                ret.components.push(component);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return ret;
    }

    public static NewInstructionActivityExtra fromComponent(InstructionComponent component) {
        LinkedList<InstructionComponent> components = new LinkedList<>();
        components.add(component);
        return fromComponents(components);
    }

    public static NewInstructionActivityExtra fromComponents(List<InstructionComponent> components) {
        NewInstructionActivityExtra retExtra = new NewInstructionActivityExtra();
        retExtra.components = (LinkedList<InstructionComponent>) components;
        return retExtra;
    }

    public String toJson() {
        String ret = String.format("{'%s':[%s]}", Components, "%s");
        for (int i = 0; i < components.size(); i++) {

            ret = String.format(ret, String.format("{'%s':%s,'%s':%s,'%s':%s,'%s':%s,'%s':'%s'},%s",
                    StartX, components.get(i).startX,
                    StartY, components.get(i).startY,
                    Width, components.get(i).width,
                    Height, components.get(i).height,
                    Text, components.get(i).text, "%s"));
        }

        return ret.replace(",%s", "");


    }


}


