package ru.senkocraft.log;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;

public class Main extends JavaPlugin implements Listener {

    // Создание файла, который будет конфигом
    File config = new File(getDataFolder() + File.separator + "config.yml");

    // Метод, который выполнится когда плагин запустится
    public void onEnable() {
        // Регистрируем ивенты (события)
        Bukkit.getServer().getPluginManager().registerEvents(this, this);
        // Проверка на то, существует ли файл конфига в папке сервера
        if(!config.exists()) {
            // Если его не существует: копируем стандартный конфиг из папки проекта, которая отмечена как ресурсы
            getConfig().options().copyDefaults(true);
            // Сохраняем стандартный конфиг
            saveDefaultConfig();
        }
    }

    @EventHandler // Отмечаем, что этот метод использует ивент; здесь используем ивент, который выполняется перед вводом команды игроком
    public void onCommand(PlayerCommandPreprocessEvent e) {
        // Переменная true/false, которая обозначает, заблокирована ли введёная команда
        Boolean isCommandBlacklisted = false;

        // Цикл, который по очереди просматривает все строки из списка "blacklisted-commands" в конфиге
        for(String s : getConfig().getStringList("blacklisted-commands")) {
            // если(ивент.получитьСообщение().разделить(" ")[0].равенИгнорируяРегистр(строка из цикла)
            // Если первая часть сообщения(команды) (части делятся по пробелам), введёного игроком равна строке из цикла, то выполняется следующее
            if(e.getMessage().split(" ")[0].equalsIgnoreCase(s)) {
                // Отмечаем, что команда, которую ввёл игрок - заблокирована
                isCommandBlacklisted = true;
            }
        }

        // Если команда не заблокирована
        if(!isCommandBlacklisted) {
            // Цикл, который по очереди просматривает всех онлайн игроков на сервере
            for(Player pl : Bukkit.getServer().getOnlinePlayers()) {
                // Получаем из конфига список ников игроков, которые будут получать наше сообщение, и проверям, содержит ли этот список ник игрока из цикла
                // Также проверяется не равен ли ник отправителя - нику из цикла
                if(getConfig().getStringList("log_to").contains(pl.getName()) && !pl.getName().equalsIgnoreCase(e.getPlayer().getName())) {
                    // Если условия выполнены - отправляем лог-сообщение человеку из цикла
                    pl.sendMessage("§7(§cЛог§7) §aИгрок §c" + e.getPlayer().getName() + "§a использовал команду §c" + e.getMessage());
                }
            }
        }
    }

    // Показываем, что метод использует ивент (в данном случае - ивент выхода игрока)
    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent e) {

        // Цикл, который по очереди просматривает всех онлайн игроков
        for(Player pl : Bukkit.getServer().getOnlinePlayers()) {
            // Получаем из конфига список ников игроков, которые будут получать наше сообщение, и проверям, содержит ли этот список ник игрока из цикла
            // Также проверяется не равен ли ник игрока - нику из цикла
            if(getConfig().getStringList("log_to").contains(pl.getName()) && !pl.getName().equalsIgnoreCase(e.getPlayer().getName())) {
                // Если условия выполнены - отправляем лог-сообщение игроку из цикла
                pl.sendMessage("§7(§cЛог§7) §aИгрок §c" + e.getPlayer().getName() + "§a покинул игру §c(§r" + e.getQuitMessage() + "§c)");
            }
        }
    }

    // Показываем, что метод использует ивент (в данном случае - ивент вылета игрока)
    @EventHandler
    public void onPlayerKick(PlayerKickEvent e) {

        // Цикл, который по очереди просматривает всех онлайн игроков
        for(Player pl : Bukkit.getServer().getOnlinePlayers()) {
            // Получаем из конфига список ников игроков, которые будут получать наше сообщение, и проверям, содержит ли этот список ник игрока из цикла
            // Также проверяется не равен ли ник игрока - нику из цикла
            if(getConfig().getStringList("log_to").contains(pl.getName()) && !pl.getName().equalsIgnoreCase(e.getPlayer().getName())) {
                // Если условия выполнены - отправляем лог-сообщение с причиной вылета игрока
                pl.sendMessage("§7(§cЛог§7) §aИгрок §c" + e.getPlayer().getName() + "§a покинул игру §c(§r" + e.getReason() + "§c)");
            }
        }
    }


}
