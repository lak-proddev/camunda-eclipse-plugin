package org.eclipse.bpmn2.modeler.core.test.feature.delete;

import static org.eclipse.bpmn2.modeler.core.test.util.operations.DeleteParticipantOperation.deleteParticipant;
import static org.fest.assertions.api.Assertions.assertThat;

import java.util.List;

import org.eclipse.bpmn2.Collaboration;
import org.eclipse.bpmn2.Process;
import org.eclipse.bpmn2.modeler.core.di.DIUtils;
import org.eclipse.bpmn2.modeler.core.test.feature.AbstractFeatureTest;
import org.eclipse.bpmn2.modeler.core.test.util.DiagramResource;
import org.eclipse.bpmn2.modeler.core.test.util.Util;
import org.eclipse.bpmn2.modeler.core.utils.BusinessObjectUtil;
import org.eclipse.bpmn2.modeler.core.utils.ModelUtil;
import org.eclipse.dd.di.DiagramElement;
import org.eclipse.graphiti.mm.pictograms.Shape;
import org.junit.Test;

/**
 * @author adrobisch
 */
public class DeleteParticipantFeatureTest extends AbstractFeatureTest {
	
	@Test
	@DiagramResource
	public void testDeleteSingleParticipant() {
		Shape participantShape = Util.findShapeByBusinessObjectId(getDiagram(), "_Participant_2");
		deleteParticipant(participantShape, diagramTypeProvider).execute();
		assertThat(ModelUtil.getAllRootElements(getDefinitions(), Collaboration.class)).hasSize(0);
		
		List<Process> processes = ModelUtil.getAllRootElements(getDefinitions(), org.eclipse.bpmn2.Process.class);
		
		assertThat(processes).hasSize(1);
		assertThat(processes.get(0).getFlowElements()).hasSize(0);
		
		DiagramElement participantDi = DIUtils.findDiagramElement(getDefinitions().getDiagrams(), BusinessObjectUtil.getFirstBaseElement(participantShape));
		assertThat(participantDi).isNull();
	}
	
	@Test
	@DiagramResource
	public void testDeleteMultipleParticipants() {
		Shape participantShape = Util.findShapeByBusinessObjectId(getDiagram(), "Participant_4");
		Shape task2Shape = Util.findShapeByBusinessObjectId(getDiagram(), "Task_2");
		
		DiagramElement participantDi = DIUtils.findDiagramElement(getDefinitions().getDiagrams(), BusinessObjectUtil.getFirstBaseElement(participantShape));
		assertThat(participantDi).isNotNull();
		
		List<Process> beforeProcesses = ModelUtil.getAllRootElements(getDefinitions(), org.eclipse.bpmn2.Process.class);
		assertThat(beforeProcesses).hasSize(2);
		
		deleteParticipant(participantShape, diagramTypeProvider).execute();
		
		assertThat(ModelUtil.getAllRootElements(getDefinitions(), Collaboration.class)).hasSize(1);
		
		List<Process> afterProcesses = ModelUtil.getAllRootElements(getDefinitions(), org.eclipse.bpmn2.Process.class);
		
		assertThat(afterProcesses).hasSize(1);
		assertThat(afterProcesses.get(0).getFlowElements()).hasSize(1);
		
		participantDi = DIUtils.findDiagramElement(getDefinitions().getDiagrams(), BusinessObjectUtil.getFirstBaseElement(participantShape));
		assertThat(participantDi).isNull();
		
		DiagramElement task2Di = DIUtils.findDiagramElement(getDefinitions().getDiagrams(), BusinessObjectUtil.getFirstBaseElement(task2Shape));
		assertThat(task2Di).isNull();
	}
	
}
