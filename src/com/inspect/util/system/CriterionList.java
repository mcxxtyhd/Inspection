package com.inspect.util.system;

import java.util.ArrayList;

import org.hibernate.criterion.Criterion;

public class CriterionList extends ArrayList<Object>
{
  private static final long serialVersionUID = 1;

  public final Criterion getParas(int index)
  {
    return ((Criterion)super.get(index));
  }

  public final void addPara(int index, Criterion p)
  {
    super.add(index, p);
  }

  public final void addPara(Criterion p)
  {
    super.add(p);
  }

  public final int indexofPara(Criterion p)
  {
    return super.indexOf(p);
  }

  public final void removePara(int index)
  {
    super.remove(index);
  }
}

