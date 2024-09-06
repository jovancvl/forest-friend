package jovancvl.javabotwebsite.Bot.commands.Voice;

import net.dv8tion.jda.api.entities.Member;

public class VoiceChannelHelper {

    public static MemberVCState ifMemberAndBotInSameVC(Member m, Member b){
        if (!m.getVoiceState().inAudioChannel()){
            return MemberVCState.MEMBER_NOT_IN_VC;
        }
        if (!b.getVoiceState().inAudioChannel()){
            return MemberVCState.BOT_NOT_IN_VC;
        }
        if (m.getVoiceState().getChannel() != b.getVoiceState().getChannel()){
            return MemberVCState.DIFFERENT_VC;
        }
        return MemberVCState.SAME_VC;

    }
}
