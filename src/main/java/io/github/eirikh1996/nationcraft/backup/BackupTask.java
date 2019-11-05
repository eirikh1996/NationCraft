package io.github.eirikh1996.nationcraft.backup;

import com.google.common.io.Files;
import io.github.eirikh1996.nationcraft.NationCraft;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.File;
import java.io.IOException;

public class BackupTask extends BukkitRunnable {
    @Override
    public void run() {
        File backupDir = new File(NationCraft.getInstance().getDataFolder(), "backup");
        if (!backupDir.exists()){
            backupDir.mkdirs();
        }
        for (File toBackup : NationCraft.getInstance().getDataFolder().listFiles()){
            if (toBackup == null){
                continue;
            } else if (toBackup.getName().equalsIgnoreCase("backup")){
                continue;
            }
            try {
                Files.copy(backupDir, toBackup);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
