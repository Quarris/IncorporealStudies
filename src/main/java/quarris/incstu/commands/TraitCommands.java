package quarris.incstu.commands;

import com.mojang.brigadier.CommandDispatcher;
import net.minecraft.command.CommandSource;
import net.minecraft.command.arguments.EntityArgument;
import net.minecraft.command.arguments.NBTCompoundTagArgument;
import net.minecraft.command.arguments.ResourceLocationArgument;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import quarris.incstu.traits.ITrait;
import quarris.incstu.traits.ITraitHolder;
import quarris.incstu.traits.TraitSystem;

import java.util.Set;
import java.util.stream.Collectors;

import static net.minecraft.command.Commands.argument;
import static net.minecraft.command.Commands.literal;

public class TraitCommands {

    private static final ITextComponent ADD_SUCCESS_RESPONSE = new TranslationTextComponent("incstu.commands.add.success");
    private static final ITextComponent ADD_EXISTS_RESPONSE = new TranslationTextComponent("incstu.commands.add.exists");
    private static final ITextComponent ADD_MISSING_RESPONSE = new TranslationTextComponent("incstu.commands.add.missing");

    private static final ITextComponent REMOVE_SUCCESS_RESPONSE = new TranslationTextComponent("incstu.commands.remove.success");
    private static final ITextComponent REMOVE_NOT_EXIST_RESPONSE = new TranslationTextComponent("incstu.commands.remove.not_exists");
    private static final ITextComponent REMOVE_MISSING_RESPONSE = new TranslationTextComponent("incstu.commands.remove.missing");

    private static final ITextComponent LIST_EMPTY_RESPONSE = new TranslationTextComponent("incstu.commands.list.empty");
    private static final ITextComponent LIST_NOT_EMPTY_RESPONSE = new TranslationTextComponent("incstu.commands.list.not_empty");

    public static void register(CommandDispatcher<CommandSource> dispatcher) {
        dispatcher.register(literal("traits").requires(source -> source.hasPermissionLevel(2))
                .then(argument("player", EntityArgument.player())
                        .then(literal("add")
                                .then(argument("trait", ResourceLocationArgument.resourceLocation())
                                        .suggests((command, suggestions) -> {
                                            for (ResourceLocation res : TraitSystem.traitKeys()) {
                                                suggestions.suggest(res.toString());
                                            }
                                            return suggestions.buildFuture();
                                        })
                                        .executes(command -> addTrait(command.getSource(), EntityArgument.getPlayer(command, "player"), ResourceLocationArgument.getResourceLocation(command, "trait"), null))
                                        .then(argument("nbt", NBTCompoundTagArgument.nbt())
                                                .executes(command -> addTrait(command.getSource(), EntityArgument.getPlayer(command, "player"), ResourceLocationArgument.getResourceLocation(command, "trait"), NBTCompoundTagArgument.getNbt(command, "nbt"))))))
                        .then(literal("remove")
                                .then(argument("trait", ResourceLocationArgument.resourceLocation())
                                        .suggests((command, suggestions) -> {
                                            EntityArgument.getPlayer(command, "player").getCapability(TraitSystem.CAPABILITY).ifPresent(cap -> {
                                                for (ResourceLocation name : cap.getActiveTraits().keySet()) {
                                                    suggestions.suggest(name.toString());
                                                }
                                            });
                                            return suggestions.buildFuture();
                                        })
                                        .executes(command -> removeTrait(command.getSource(), EntityArgument.getPlayer(command, "player"), ResourceLocationArgument.getResourceLocation(command, "trait")))))
                        .then(literal("list")
                                .executes(command -> listTraits(command.getSource(), EntityArgument.getPlayer(command, "player")))))
                .then(literal("add")
                        .then(argument("trait", ResourceLocationArgument.resourceLocation())
                                .suggests((command, suggestions) -> {
                                    for (ResourceLocation res : TraitSystem.traitKeys()) {
                                        suggestions.suggest(res.toString());
                                    }
                                    return suggestions.buildFuture();
                                })
                                .executes(command -> addTrait(command.getSource(), command.getSource().asPlayer(), ResourceLocationArgument.getResourceLocation(command, "trait"), null))
                                .then(argument("nbt", NBTCompoundTagArgument.nbt())
                                        .executes(command -> addTrait(command.getSource(), command.getSource().asPlayer(), ResourceLocationArgument.getResourceLocation(command, "trait"), NBTCompoundTagArgument.getNbt(command, "nbt"))))))
                .then(literal("remove")
                        .then(argument("trait", ResourceLocationArgument.resourceLocation())
                                .suggests((command, suggestions) -> {
                                    command.getSource().asPlayer().getCapability(TraitSystem.CAPABILITY).ifPresent(cap -> {
                                        for (ResourceLocation name : cap.getActiveTraits().keySet()) {
                                            suggestions.suggest(name.toString());
                                        }
                                    });
                                    return suggestions.buildFuture();
                                })
                                .executes(command -> removeTrait(command.getSource(), command.getSource().asPlayer(), ResourceLocationArgument.getResourceLocation(command, "trait")))))
                .then(literal("list")
                        .executes(command -> listTraits(command.getSource(), command.getSource().asPlayer()))));
    }

    private static int listTraits(CommandSource source, ServerPlayerEntity player) {
        if (player.getCapability(TraitSystem.CAPABILITY).isPresent()) {
            player.getCapability(TraitSystem.CAPABILITY).ifPresent(cap -> {
                Set<ResourceLocation> traitNames = cap.getActiveTraits().keySet();
                if (traitNames.isEmpty()) {
                    source.sendFeedback(LIST_EMPTY_RESPONSE, false);
                } else {
                    source.sendFeedback(LIST_NOT_EMPTY_RESPONSE, false);
                    String traitNamesString = String.join(", ", traitNames.stream().map(ResourceLocation::toString).collect(Collectors.toSet()));
                    source.sendFeedback(new StringTextComponent(traitNamesString), false);
                }
            });
            return 1;
        }

        return 0;
    }

    private static int addTrait(CommandSource source, ServerPlayerEntity player, ResourceLocation name, CompoundNBT nbt) {
        ITrait<PlayerEntity> trait = TraitSystem.createTrait(player, name);

        if (trait != null) {
            if (nbt != null)
                trait.deserializeNBT(nbt);
            ITraitHolder traitHolder = player.getCapability(TraitSystem.CAPABILITY).orElse(null);

            if (traitHolder != null) {
                if (traitHolder.hasTrait(name)) {
                    source.sendFeedback(ADD_EXISTS_RESPONSE, false);
                    return 0;
                } else {
                    traitHolder.addTrait(name, trait);
                    source.sendFeedback(ADD_SUCCESS_RESPONSE, false);
                    return 1;
                }
            }
            return 0;
        }

        source.sendFeedback(ADD_MISSING_RESPONSE, false);
        return 0;
    }

    private static int removeTrait(CommandSource source, ServerPlayerEntity player, ResourceLocation name) {
        if (TraitSystem.isRegistered(name)) {
            ITraitHolder traitHolder = player.getCapability(TraitSystem.CAPABILITY).orElse(null);

            if (traitHolder != null) {
                if (!traitHolder.hasTrait(name)) {
                    source.sendFeedback(REMOVE_NOT_EXIST_RESPONSE, false);
                    return 0;
                } else {
                    traitHolder.removeTrait(name);
                    source.sendFeedback(REMOVE_SUCCESS_RESPONSE, false);
                    return 1;
                }
            }
            return 1;
        }

        source.sendFeedback(REMOVE_MISSING_RESPONSE, false);
        return 0;
    }


}
