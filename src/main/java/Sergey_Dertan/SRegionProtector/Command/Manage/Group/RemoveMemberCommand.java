package Sergey_Dertan.SRegionProtector.Command.Manage.Group;

import Sergey_Dertan.SRegionProtector.Command.SRegionProtectorCommand;
import Sergey_Dertan.SRegionProtector.Region.Region;
import Sergey_Dertan.SRegionProtector.Region.RegionManager;
import cn.nukkit.Player;
import cn.nukkit.command.CommandSender;

import java.util.Map;

public final class RemoveMemberCommand extends SRegionProtectorCommand {

    private RegionManager regionManager;

    public RemoveMemberCommand(String name, Map<String, String> messages, RegionManager regionManager) {
        super(name, messages);
        this.regionManager = regionManager;
    }

    @Override
    public boolean execute(CommandSender sender, String s, String[] args) {
        if (!this.testPermission(sender)) return false;
        if (args.length < 2) {
            sender.sendMessage(this.usageMessage);
            return false;
        }

        Region region = this.regionManager.getRegion(args[0].toLowerCase());
        if (region == null) {
            this.sendMessage(sender, "region-doesnt-exists", "@region", args[0]);
            return false;
        }

        String target = args[1].toLowerCase();
        if (target.equals("")) {
            sender.sendMessage(this.usageMessage);
            return false;
        }
        if ((sender instanceof Player && !region.isOwner(sender.getName().toLowerCase())) && !sender.hasPermission("sregionprotector.admin")) {
            sender.sendMessage(this.getPermissionMessage());
            return false;
        }
        if (!region.isMember(target)) {
            this.sendMessage(sender, "not-a-member", new String[]{"@region", "@target"}, new String[]{region.getName(), target});
            return false;
        }
        this.regionManager.removeMember(region, target);
        this.sendMessage(sender, "member-removed", new String[]{"@region", "@target"}, new String[]{region.getName(), target});
        return true;
    }
}