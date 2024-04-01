package fr.anthonyquere.talkwithme.minecraftmod.neighbor.goals;

import fr.anthonyquere.talkwithme.minecraftmod.neighbor.NeighborEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.MoveToBlockGoal;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.BedBlock;
import net.minecraft.world.level.block.state.properties.BedPart;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import org.jetbrains.annotations.NotNull;

import java.util.EnumSet;

public class SleepInBedGoal extends MoveToBlockGoal {
    private final NeighborEntity mob;
    private long lastGoodNight;

    public SleepInBedGoal(NeighborEntity mob) {
        super(mob, 1, 16);
        this.mob = mob;
        this.setFlags(EnumSet.of(Goal.Flag.JUMP, Goal.Flag.MOVE));
        System.out.println(this.mob.level().getGameTime());
        this.lastGoodNight = getDay() - 1;
    }

    @Override
    public boolean canUse() {
        return isNight() && this.findNearestBlock();
    }

    private int getDay() {
        return (int) (this.mob.level().getDayTime() / 24000L % 2147483647L);
    }

    private boolean isNight() {
        return this.mob.level().isNight();
    }

    @Override
    protected boolean isValidTarget(LevelReader levelReader, @NotNull BlockPos blockPos) {
        var block = levelReader.getBlockState(blockPos);

        return block.is(BlockTags.BEDS)
                && isNight()
                && block.getValue(BlockStateProperties.BED_PART).equals(BedPart.HEAD)
                && (this.mob.isSleeping() || !block.getValue(BedBlock.OCCUPIED));
        //&& levelReader.isEmptyBlock(blockPos.above());
    }

    @Override
    public void stop() {
        super.stop();
        if (this.mob.isSleeping()) {
            this.mob.stopSleeping();
        }
    }


    @Override
    public void tick() {
        super.tick();

        if (!this.isReachedTarget()) {
            this.mob.stopSleeping();
        } else if (!this.mob.isSleeping()) {
            var today = getDay();
            if (today != lastGoodNight) {
                lastGoodNight = today;

                this.mob.getNearbyPlayers().forEach(player -> player.displayClientMessage(Component.literal("Good night!"), false));

                System.out.println("Good night!!!! " + today);
            }
            this.mob.startSleeping(this.blockPos);
        }

    }
}
