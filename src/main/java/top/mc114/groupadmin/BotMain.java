package top.mc114.groupadmin;

import net.mamoe.mirai.console.command.BlockingCommand;
import net.mamoe.mirai.console.command.CommandSender;
import net.mamoe.mirai.console.command.JCommandManager;
import net.mamoe.mirai.console.plugins.Config;
import net.mamoe.mirai.console.plugins.PluginBase;
import net.mamoe.mirai.event.events.MemberJoinRequestEvent;
import net.mamoe.mirai.event.events.MemberLeaveEvent;
import net.mamoe.mirai.message.GroupMessage;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class BotMain extends PluginBase {
    public static List<String> key_list;
    public static List<String> accept_list;
    public static Config config;
    public void onLoad() {
        config = loadConfig("config.yml");//加载config

        List<String> keyword_list = new ArrayList<>();//新建一个临时list
        Collections.addAll(keyword_list,"ddos","傻逼","翻墙","vpn",
                "ssr","v2ray","社工","人肉","梯子","脑瘫","你妈");
        config.setIfAbsent("key_words", keyword_list);//如果config不存在，则将临时list内容导入config

        List<String> acceptword_list = new ArrayList<>();
        Collections.addAll(acceptword_list, "bdx", "bedrockx");
        config.setIfAbsent("accept_words", acceptword_list);

        config.save();
        acceptword_list.clear();
        keyword_list.clear();//删除defaultlist中的所有元素
        key_list = config.getStringList("key_words");
        accept_list = config.getStringList("accept_words");
    }

    public void onEnable() {
        this.getEventListener().subscribeAlways(GroupMessage.class, new GroupMessageListener());
        this.getEventListener().subscribeAlways(MemberLeaveEvent.class, new GroupMemberLeaveListener());
        this.getEventListener().subscribeAlways(MemberJoinRequestEvent.class, new GroupMemberRequest());
        JCommandManager.getInstance().register(this, new BlockingCommand(
                "gareload", new ArrayList<>(),"重载GA配置文件","/gareload"
        ) {
            @Override
            public boolean onCommandBlocking(@NotNull CommandSender commandSender, @NotNull List<String> list) {
                config = loadConfig("config.yml");
                key_list = config.getStringList("key_words");
                accept_list = config.getStringList("accept_words");
                commandSender.sendMessageBlocking("重载成功");
                return true;
            }
        });
    }
}