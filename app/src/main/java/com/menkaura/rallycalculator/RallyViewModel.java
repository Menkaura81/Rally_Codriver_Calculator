// RallyViewModel.java
package com.menkaura.rallycalculator;

import androidx.lifecycle.ViewModel;

public class RallyViewModel extends ViewModel {
    public String horasSalida = "";
    public String minutosSalida = "";
    public String horasTransito = "";
    public String minutosTransito = "";
    public String horasATC = "";
    public String minutosATC = "";
    // Rally Time offset
    public int hOffset = 0;
    public int mOffset = 0;
    public int sOffset = 0;
}
