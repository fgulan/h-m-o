package algorithms.genetic.mutation;

import algorithms.greedy.Solution;
import models.Machine;
import models.Task;
import models.TaskTimeEntry;
import utils.ArrayUtils;
import utils.Pair;
import utils.RandUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class PermutationMutation implements IMutationOperator {

    private final float prob;
    private final int maxSwaps;

    public PermutationMutation(float prob, int maxSwaps) {
        this.prob = prob;
        this.maxSwaps = maxSwaps;
    }

    @Override
    public Solution mutate(Solution solution) {
        RandUtils rand = RandUtils.getInstance();
        if (rand.nextFloat() > prob) {
            return solution;
        }
        int leftSwaps = maxSwaps;

        Map<Machine, List<TaskTimeEntry>> mtt = solution.getMachineTimeTable();
        List<Machine> machines = new ArrayList<>(mtt.keySet());
//        Collections.shuffle(machines);

        for (Machine machine : machines) {
            if (leftSwaps <= 0) {
                break;
            } else if (rand.nextFloat() > prob) {
                continue;
            }
            List<Task> tasks = mtt.get(machine)
                    .stream()
                    .map(TaskTimeEntry::getTask)
                    .collect(Collectors.toList());


            if (tasks.isEmpty()) {
                continue;
            }
            Pair<Integer, Integer> pair = rand.nextIntPair(tasks.size());
            if (pair.getFirst() == pair.getSecond()) {
                continue;
            }
            Collections.swap(tasks, pair.getFirst(), pair.getSecond());
            List<TaskTimeEntry> entries = new ArrayList<>();
            TaskTimeEntry previousEntry = null;
            for (Task task : tasks) {
                TaskTimeEntry entry = null;
                if (previousEntry == null) {
                    entry = new TaskTimeEntry(task);
                } else {
                    entry = new TaskTimeEntry(previousEntry, task);
                }
                entries.add(entry);
                previousEntry = entry;
            }

            mtt.put(machine, entries);
            leftSwaps -= 1;
        }
        return solution;
    }
}
