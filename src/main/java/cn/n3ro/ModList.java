package cn.n3ro;

import java.util.ArrayList;

public class ModList<T> extends ArrayList<T> {

    public boolean needInterupt = true;
    @Override
    public boolean add(Object object) {
        boolean res = super.add((T) object);
        if (needInterupt) {

            int index = -1;
            for(int i = 0; i < super.size(); i++) {
                if (super.get(i).getClass().getName().equals("com.xue.vapu.ClassTransformer")) {
                    index = i;
                    break;
                }
            }


            Object myClassTransformer = null;
            if (index >= 0) {
                myClassTransformer = (Object) super.remove(index);
            }

            if (myClassTransformer != null) {
                super.add(super.size(), (T) myClassTransformer);
            }

        }
        return res;
    }
}
