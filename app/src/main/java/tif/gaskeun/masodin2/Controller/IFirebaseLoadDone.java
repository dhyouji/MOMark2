package tif.gaskeun.masodin2.Controller;

import java.util.List;

import tif.gaskeun.masodin2.Model.Informasi;

public interface IFirebaseLoadDone {
    void onFirebaseLoadSuccess(List<Informasi> infox);
    void onFirebaseLoadFailed(String Message);
}
